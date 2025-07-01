package com.example.findyourself.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findyourself.dataClasses.ConnectResponse
import com.example.findyourself.dataClasses.User
import com.example.findyourself.repositories.ConnectRepository
import com.example.findyourself.repositories.FirebaseConnectChatRepository
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton


sealed class ConnectUiState {
    object Idle : ConnectUiState()
    object Loading : ConnectUiState()
    data class Success(val response: ConnectResponse) : ConnectUiState()
    data class Failure(val message: String) : ConnectUiState()
}

sealed class LoadChatUserUiState {
    object Idle : LoadChatUserUiState()
    object Loading : LoadChatUserUiState()
    data class Success(val user: User) : LoadChatUserUiState()
    data class Failure(val message: String) : LoadChatUserUiState()
}

sealed class CreateConnectChatUiState {
    object Idle : CreateConnectChatUiState()
    object Loading : CreateConnectChatUiState()
    data class Success(val message: String) : CreateConnectChatUiState()
    data class Failure(val message: String) : CreateConnectChatUiState()
}

@Singleton
class ConnectViewModel @Inject constructor(
    private val connectRepository: ConnectRepository,
    private val firebaseConnectRepo : FirebaseConnectChatRepository
) : ViewModel() {

    private val _connectState = MutableSharedFlow<ConnectUiState>()
    val connectState: SharedFlow<ConnectUiState> = _connectState

    fun connect(userId: String, interests: List<String>) {
        viewModelScope.launch {
            _connectState.emit(ConnectUiState.Loading)

            try {
                withTimeout(60_000L) { // Wait up to 5 minutes
                    while (true) {
                        val result = connectRepository. connectToStranger(userId, interests)
                        val response = result.getOrNull()
                        Log.d("Jaipur99","Response : $response")
                        if (result.isSuccess && response != null && response.isConnected) {
                            _connectState.emit(ConnectUiState.Idle)
                            _connectState.emit(ConnectUiState.Success(response))
                            return@withTimeout
                        }

                        delay(3000L) // Try again after 3s
                    }
                }
            } catch (e: TimeoutCancellationException) {
                _connectState.emit(ConnectUiState.Failure("No match found in 5 minutes."))
            } catch (e: Exception) {
                _connectState.emit(ConnectUiState.Failure("Error Connecting: ${e.localizedMessage}"))
            }
        }
    }



    private val _chatUser = MutableSharedFlow<LoadChatUserUiState>()
    val chatUser: SharedFlow<LoadChatUserUiState> = _chatUser

    private val _participant = MutableStateFlow<User?>(null)
    val participant : StateFlow<User?> = _participant

    fun loadChatUser(userId: String) {
        viewModelScope.launch {
            try {
                _chatUser.emit(LoadChatUserUiState.Loading)
                val result = connectRepository.getChatUser(userId)
                _chatUser.emit(LoadChatUserUiState.Idle)
                if (result.isSuccess && result.getOrNull() != null) {
                    Log.d("ConnectJaipur", "Fetched Chat User : ${result.getOrThrow()}")
                    _chatUser.emit(LoadChatUserUiState.Success(result.getOrThrow()))
                    _participant.value = result.getOrNull()
                } else {
                    Log.d("ConnectJaipur", "Error Fetching Chat User : ${result.exceptionOrNull()?.message}")
                    _chatUser.emit(LoadChatUserUiState.Failure("Error Fetching Chat User !"))
                    return@launch
                }
            } catch (e: Exception) {
                Log.d("ConnectJaipur", "Exception Fetching Chat User : ${e.message}")
                _chatUser.emit(LoadChatUserUiState.Failure("Error Loading Chat User !"))
            }
        }
    }

    private val _createConnectChatUiState = MutableSharedFlow<CreateConnectChatUiState>()
    val createConnectChatUiState: SharedFlow<CreateConnectChatUiState> = _createConnectChatUiState

    fun createConnectChat(chatId: String, user1: User, user2: User) {
        viewModelScope.launch {
            try {
                _createConnectChatUiState.emit(CreateConnectChatUiState.Loading)
                val result1 = firebaseConnectRepo.createChatDocumentIfNeeded(chatId, user1, user2)
                _createConnectChatUiState.emit(CreateConnectChatUiState.Idle)

                if (result1.isSuccess && result1.getOrNull() != null) {
                    Log.d("ConnectJaipur", "Success Creating Chat : ${result1.getOrNull()}")
                    _createConnectChatUiState.emit(CreateConnectChatUiState.Success("Chat Created Successfully !"))

                } else {
                    Log.d("ConnectJaipur", "Error Creating Chat : ${result1.exceptionOrNull()?.message}")
                    _createConnectChatUiState.emit(CreateConnectChatUiState.Failure("Error Creating Chat Document!"))
                }
            } catch (e: Exception) {
                _createConnectChatUiState.emit(CreateConnectChatUiState.Failure("Error Creating Chat !"))
                Log.d("ConnectJaipur", "Exception Creating Chat : ${e.message}")
                return@launch
            }
        }
    }

    fun clearState() {
        viewModelScope.launch {
            _connectState.emit(ConnectUiState.Idle)
            _chatUser.emit(LoadChatUserUiState.Idle)
            _createConnectChatUiState.emit(CreateConnectChatUiState.Idle)
            _participant.value = null
        }
    }
}