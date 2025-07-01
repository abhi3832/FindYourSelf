package com.example.findyourself.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.findyourself.screens.bottomNavigatoinScreens.ConnectScreen
import com.example.findyourself.screens.bottomNavigatoinScreens.HistoryScreen
import com.example.findyourself.screens.bottomNavigatoinScreens.HomeScreen
import com.example.findyourself.screens.bottomNavigatoinScreens.InboxScreen
import com.example.findyourself.screens.bottomNavigatoinScreens.ProfileScreen
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.ConnectChatViewModel
import com.example.findyourself.viewModels.ConnectViewModel
import com.example.findyourself.viewModels.MessageViewModel
import com.example.findyourself.viewModels.UserViewModel

@Composable
fun MainNavGraph(
    mainNavController: NavHostController,
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    connectViewModel: ConnectViewModel,
    connectChatViewModel: ConnectChatViewModel,
    messageViewModel: MessageViewModel
) {
    NavHost(
         navController = mainNavController,
        route = Graphs.MAINNAVGRAPH,
        startDestination = MainNavScreens.ChatScreen.route
    ) {
        composable(MainNavScreens.ChatScreen.route) { HomeScreen(mainNavController, rootNavController) }
        composable(MainNavScreens.ConnectScreen.route) { ConnectScreen(mainNavController, rootNavController,connectViewModel,userViewModel, connectChatViewModel) }
        composable(MainNavScreens.ProfileScreen.route) { ProfileScreen(mainNavController, rootNavController, authViewModel, userViewModel) }
        composable(MainNavScreens.HistoryScreen.route) { HistoryScreen(mainNavController, rootNavController, connectViewModel,userViewModel, connectChatViewModel) }
        composable(MainNavScreens.NotificationScreen.route) { InboxScreen(mainNavController, rootNavController) }
    }
}