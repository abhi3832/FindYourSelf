package com.example.findyourself.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findyourself.dataClasses.Message
import com.example.findyourself.repositories.FirebaseMessageRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


//sealed class SendStatus {
//    object Idle : SendStatus()
//    object Success : SendStatus()
//    data class Error(val throwable: Throwable) : SendStatus()
//}

@Singleton
class MessageViewModel @Inject constructor(private val messageRepository: FirebaseMessageRepository) : ViewModel() {


    private val _sendStatus = MutableSharedFlow<Result<Unit>>()
    val sendStatus: SharedFlow<Result<Unit>> = _sendStatus

    fun sendMessage(chatId: String, message: Message){
        viewModelScope.launch {
            try {
                messageRepository.sendMessage(chatId, message).collect {
                    _sendStatus.emit(it)
                }
            }catch (e : Exception){

            }
        }
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun startObservingMessages(chatId: String) {
        viewModelScope.launch {
            messageRepository.observeMessages(chatId)
                .collect { messageList ->
                    _messages.value = messageList
                }
        }
    }

    fun clearState(){
        _messages.value = emptyList()
    }
}