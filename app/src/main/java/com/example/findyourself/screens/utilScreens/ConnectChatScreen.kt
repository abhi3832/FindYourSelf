package com.example.findyourself.screens.utilScreens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.findyourself.dataClasses.Message
import com.example.findyourself.dataClasses.User
import com.example.findyourself.navigation.UserProfileDetailScreen
import com.example.findyourself.repositories.ActiveChat
import com.example.findyourself.screens.authScreens.showToast
import com.example.findyourself.screens.utils.AlertDialogForEndingChat
import com.example.findyourself.viewModels.ConnectChatViewModel
import com.example.findyourself.viewModels.ConnectViewModel
import com.example.findyourself.viewModels.MessageViewModel
import com.example.findyourself.viewModels.UserViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.String

@Composable
fun ConnectChatScreen(
    chatId: String,
    rootNavController: NavHostController,
    connectViewModel: ConnectViewModel,
    connectChatViewModel: ConnectChatViewModel,
    userViewModel: UserViewModel,
    messageViewModel: MessageViewModel
) {

    val messages by messageViewModel.messages.collectAsState()

    LaunchedEffect(chatId) {
        messageViewModel.startObservingMessages(chatId)
    }


    val myUser = userViewModel.user.value
    val participant = connectViewModel.participant.collectAsState()
    val activeChat by connectChatViewModel.activeChat.collectAsState()

    val context = LocalContext.current

    val typingUsers by connectChatViewModel.typingUsers.collectAsState()

    LaunchedEffect(chatId) {
        connectChatViewModel.observeTypingStatus(chatId)
        connectChatViewModel.startDebouncedTyping(chatId, myUser!!.uid)
    }

    DisposableEffect(chatId) {
        onDispose {
            connectChatViewModel.stopDebouncedTyping()
            connectChatViewModel.stopObservingTypingStatus(chatId)
            connectChatViewModel.userTyping(chatId, myUser!!.uid, false)
        }
    }

    if (typingUsers.any { it.value }) {
        Log.d("TypingStatus", "${typingUsers.keys} is ${typingUsers.values}")
    }


    LaunchedEffect(chatId) {
        connectChatViewModel.observeActiveChat(chatId, myUser?.uid ?: "")
    }

    BackHandler {
        if(activeChat != null && activeChat!!.isEnded){
            connectViewModel.clearState()
            rootNavController.popBackStack()
            connectChatViewModel.exitChat(
                chatId = activeChat?.chatId ?: "",
                userId = participant.value?.uid ?: ""
            )
            messageViewModel.clearState()
        }
        else if(activeChat != null && !activeChat!!.isEnded){
            rootNavController.popBackStack()
        }
    }


    // clear states only on clicking ending chat

    LaunchedEffect(activeChat){
        Log.d("CheckChat","Active Chat : $activeChat")
    }

    val openAlertDialog = remember { mutableStateOf(false) }
    val whoEndedChat = remember {mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background).imePadding().statusBarsPadding().navigationBarsPadding()
    ) {
        if(activeChat != null && !activeChat!!.isEnded){
            val userName = participant.value?.username ?: ""
            val userId = participant.value
                ?.uid ?: ""
            TopBar(rootNavController,
                openAlertDialog,userName, userId, chatId, myUser?.uid ?: "",
                connectChatViewModel,connectViewModel, whoEndedChat, activeChat,typingUsers)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                MessageList(messages = messages, participant, myUser)
                InputSection(modifier = Modifier.align(Alignment.BottomCenter), messageViewModel, activeChat, myUser, connectChatViewModel)
            }
        }else if(activeChat != null && activeChat!!.isEnded){
            val userName = participant.value?.username ?: ""
            val userId = participant.value
                ?.uid ?: ""
            TopBar(
                rootNavController,
                openAlertDialog, userName, userId, chatId, myUser?.uid ?: "",
                connectChatViewModel, connectViewModel, whoEndedChat, activeChat, typingUsers
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                MessageList(messages = messages, participant = participant, myUser = myUser)
                endedChatMessage(whoEndedChat,  Modifier.align(Alignment.BottomCenter))
            }
        }
        else{
            Text("Couldn't Create Chat!!", color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun endedChatMessage(whoEndedChat: MutableState<Boolean>, modifier: Modifier) {

    Column(modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally)
    {
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        if (whoEndedChat.value) {
            Text("You Ended the chat !!", color = MaterialTheme.colorScheme.onBackground)
        } else Text("The Participant ended the chat !!", color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
    }

}

@Composable
fun TopBar(
    rootNavController: NavHostController,
    openAlertDialog: MutableState<Boolean>,
    userName: String,
    participantId: String,
    chatId: String,
    myUserId: String,
    connectChatViewModel: ConnectChatViewModel,
    connectViewModel: ConnectViewModel,
    whoEndedChat: MutableState<Boolean>,
    activeChat: ActiveChat?,
    typingUsers: Map<String, Boolean>
) {

    val isParticipantTyping = typingUsers.any {
        it.key != myUserId && it.value
    }


    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = null, tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(30.dp).clickable {
                rootNavController.popBackStack()
            })

        // Start-aligned row
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.weight(1f).clickable {
                rootNavController.navigate(UserProfileDetailScreen.ProfileDetailScreen.createRoute(participantId)){
                    launchSingleTop = true
                }
            }, // Ensuring it takes up space
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text(text = userName, fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.onBackground, fontSize = if(isParticipantTyping) 18.sp else 24.sp)
                if(isParticipantTyping) Text(text = "typing...", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, textAlign = TextAlign.Start)
            }
        }


           Row(
               modifier = Modifier.weight(1f), // Balancing the row for proper alignment
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.End
           ) {
               if(activeChat != null && !activeChat.isEnded) {
                   Text(text = "End", color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.clickable {
                       openAlertDialog.value = !openAlertDialog.value
                   })
               }
               Spacer(modifier = Modifier.width(24.dp))
               Icon(imageVector = Icons.Default.Flag, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
           }
        
        if(openAlertDialog.value){
            AlertDialogForEndingChat(openAlertDialog, rootNavController,
                chatId, myUserId, participantId, connectChatViewModel, connectViewModel, whoEndedChat)
        }
    }
}

@Composable
fun MessageList(messages: List<Message>, participant: State<User?>, myUser: User?) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        items(messages, ) { message ->
            MessageBubble(message, participant,myUser)
        }
    }
}

@Composable
fun MessageBubble(message: Message, participant: State<User?>, myUser: User?) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        contentAlignment = if (message.senderId == myUser?.uid) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .background(
                    if (message.senderId == myUser?.uid) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = if (message.senderId == myUser?.uid) 16.dp else 4.dp,
                        topEnd = if (message.senderId == myUser?.uid) 4.dp else 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ) // Sharp corner for sender side
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.text ?: "",
                color = if (message.senderId == myUser?.uid) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun InputSection(
    modifier: Modifier,
    messageViewModel: MessageViewModel,
    activeChat: ActiveChat?,
    myUser: User?,
    connectChatViewModel: ConnectChatViewModel
) {

    val message = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val chatId = activeChat?.chatId ?: ""
    val context = LocalContext.current
    val myUser1 = myUser!!

    val typing = remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom){
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

                OutlinedTextField(
                    value = message.value,
                    onValueChange = {
                        message.value = it
                        connectChatViewModel.userTyping(chatId, myUser1.uid, it.isNotBlank())
                    },
                    modifier = Modifier
                        .weight(1f).clip(RoundedCornerShape(40.dp)),
                    shape = RoundedCornerShape(40.dp),
                    placeholder = { Text("Type a message...") },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null,
                    modifier = Modifier.size(35.dp).clickable {
                        val text = message.value.trim()
                        if (text.isEmpty()) {
                            showToast(context, "Message is empty!")
                            return@clickable
                        }

                        if (chatId.isBlank() || myUser1.uid.isBlank()) {
                            showToast(context, "Chat or User ID missing.")
                            return@clickable
                        }

                        val msg = Message(
                            messageId = UUID.randomUUID().toString(),
                            senderId = myUser1.uid,
                            text = text,
                            timestamp = System.currentTimeMillis(),
                        )

                        coroutineScope.launch {
                            messageViewModel.sendMessage(chatId, msg)
                            message.value = "" // Clear input after sending
                        }
                    }, tint = MaterialTheme.colorScheme.onBackground)
            }

    }
}