package com.example.findyourself.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigation (val screenRoute : String, val screenName : String, val screenIcon : ImageVector){
    ChatScreen("chats", "Chat", Icons.Default.Chat),
    HistoryScreen("history", "History", Icons.Default.History),
    ConnectScreen("connect", "Connect", Icons.Default.Add),
    NotificationScreen("notification", "Inbox", Icons.Default.Notifications),
    ProfileScreen("profile", "Profile", Icons.Default.AccountCircle)
}