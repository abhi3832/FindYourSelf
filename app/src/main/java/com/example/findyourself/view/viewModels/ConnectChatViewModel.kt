package com.example.findyourself.view.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findyourself.model.ActiveChat
import com.example.findyourself.data.repositories.FirebaseConnectChatStatusRepository
import com.example.findyourself.data.repositories.TypingStatusRepository
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class ActiveChatUiState {
    object Loading : ActiveChatUiState()
    data class Success(val message: String) : ActiveChatUiState()
    data class Failure(val message: String) : ActiveChatUiState()
}

sealed class EndChatUiState {
    object Idle : EndChatUiState()
    object Loading : EndChatUiState()
    data class Success(val message: String) : EndChatUiState()
    data class Failure(val message: String) : EndChatUiState()
}


class ConnectChatViewModel : ViewModel(), KoinComponent {
    private val activeChatRepository: FirebaseConnectChatStatusRepository by inject()
    private val typingStatusRepository: TypingStatusRepository by inject()


    private val _chatId = MutableStateFlow<String?>(null)
    private val _currentUserId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeChat: StateFlow<ActiveChat?> =
        combine(_chatId, _currentUserId) { chatId, userId ->
            if (chatId != null && userId != null) Pair(chatId, userId) else null
        }.flatMapLatest { pair ->
            if (pair == null) {
                flowOf(null)
            } else {
                val (chatId, userId) = pair
                activeChatRepository.observeChatAsFlow(chatId)
                    .map { chat ->
                        chat?.let {
                            val otherUserId = chat.participants.firstOrNull { it != userId } ?: return@map null
                            ActiveChat(
                                chatId = chat.chatId,
                                userId = userId,
                                participantId = otherUserId,
                                createdAt = chat.createdAt,
                                isEnded = chat.isEnded
                            )
                        }
                    }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null
        )


    fun observeActiveChat(chatId: String, userId: String) {
        _chatId.value = chatId
        _currentUserId.value = userId
    }



    fun endActiveChat() {
        val chatId = activeChat.value?.chatId ?: return

        viewModelScope.launch {
            val result = activeChatRepository.markChatAsEnded(chatId)
            if (!result) {
                Log.e("ConnectChat", "Failed to mark chat as ended.")
            }
        }
    }


    fun exitChat(chatId: String, userId: String) {
        viewModelScope.launch {
            try {
                activeChatRepository.removeUserFromParticipants(chatId, userId)
            } catch (e: Exception) {
                Log.e("ConnectChat", "Error removing user from chat: ${e.localizedMessage}")
            }
        }
    }

    fun clearState() {
        _chatId.value = null
        _currentUserId.value = null
    }

    private val _typingUsers = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val typingUsers: StateFlow<Map<String, Boolean>> = _typingUsers

    private var typingListener: ValueEventListener? = null
    private var debounceJob: Job? = null

    private val _typingFlow = MutableSharedFlow<Pair<String, String>>(extraBufferCapacity = 1)

    // Call this from Composable to start observing real-time typing
    fun observeTypingStatus(chatId: String) {
        typingListener = typingStatusRepository.observeTypingStatus(chatId) { status ->
            _typingUsers.value = status
        }
    }

    fun stopObservingTypingStatus(chatId: String) {
        typingListener?.let {
            typingStatusRepository.removeListener(chatId, it)
        }
    }

    @OptIn(FlowPreview::class)
    fun startDebouncedTyping(chatId: String, userId: String) {
        debounceJob?.cancel()

        debounceJob = viewModelScope.launch {
            _typingFlow
                .debounce(2000)
                .collect {
                    typingStatusRepository.setTypingStatus(chatId, userId, false)
                }
        }
    }

    fun stopDebouncedTyping() {
        debounceJob?.cancel()
    }

    fun userTyping(chatId: String, userId: String, isTyping: Boolean) {
        if (isTyping) {
            typingStatusRepository.setTypingStatus(chatId, userId, true)
            viewModelScope.launch {
                _typingFlow.emit(chatId to userId)
            }
        }
    }
}
