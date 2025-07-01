package com.example.findyourself.screens.bottomNavigatoinScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class Chat(
    val name: String,
    val lastMessage: String,
    val timestamp: String
)

@Composable
fun HomeScreen(mainNavController: NavHostController, rootNavController: NavHostController) {
    val chats = listOf(
        Chat("Stranger #12345", "Hey, how's it going?", "10:23 AM"),
        Chat("Stranger #67890", "Thanks for the chat!", "Yesterday"),
        Chat("Stranger #24680", "See you around!", "2 days ago")
    )

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ChatList(chats, rootNavController)
    }
}

@Composable
fun ChatList(chats: List<Chat>, rootNavController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(chats) { chat ->
            ChatItem(chat, rootNavController)
        }
    }
}

@Composable
fun ChatItem(chat: Chat, rootNavController: NavHostController) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Icon(
                imageVector = Icons.Default.Person,
                tint =  MaterialTheme.colorScheme.onBackground,
                contentDescription = "Avatar",
                modifier = Modifier.size(40.dp,)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f).clickable {
//                rootNavController.navigate(ChatDetailScreens.DetailScreen.createRoute(
//                    chatId ="123",
//                    isChatEnded = true,
//                    isHistoryChat = false
//                )){
//                    launchSingleTop = true
//                }
            }) {
                Text(text = chat.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Text(text = chat.lastMessage, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp)
            }
            Text(text = chat.timestamp, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp)
        }
    }
}


//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun LightPreview() {
//    FindYourselfTheme(darkTheme = false) {
//        HomeScreen()
//    }
//}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    FindYourselfTheme(darkTheme = true) {
//        HomeScreen()
//    }
//}