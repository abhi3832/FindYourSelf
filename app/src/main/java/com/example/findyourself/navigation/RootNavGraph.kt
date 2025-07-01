package com.example.findyourself.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.findyourself.screens.mainScreens.MainScreen
import com.example.findyourself.screens.utilScreens.ConnectChatScreen
import com.example.findyourself.screens.utilScreens.UserProfileScreenForRandomChat
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.ConnectChatViewModel
import com.example.findyourself.viewModels.ConnectViewModel
import com.example.findyourself.viewModels.MessageViewModel
import com.example.findyourself.viewModels.UserViewModel

@Composable
fun RootNavGraph(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    connectViewModel: ConnectViewModel,
    connectChatViewModel: ConnectChatViewModel,
    messageViewModel: MessageViewModel
) {

    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, route = Graphs.ROOTGRAPH,
        startDestination = if(authViewModel.onBoarded.value){
            if(authViewModel.authDone.value){
                Graphs.MAINNAVGRAPH
            }else Graphs.AUTHGRAPH
        }else Graphs.ONBOARDINGGRAPH

    ){
        authGraph(rootNavController, authViewModel, userViewModel)
        onBoardingGraph(rootNavController,authViewModel,userViewModel)
        composable(Graphs.MAINNAVGRAPH) { MainScreen(rootNavController, authViewModel, userViewModel,connectViewModel, connectChatViewModel, messageViewModel) }
        composable(UserProfileDetailScreen.ProfileDetailScreen.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )){ backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserProfileScreenForRandomChat(userId,connectViewModel)
        }
        composable(
            route = ConnectChatDetailScreen.DetailScreen.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ConnectChatScreen(chatId, rootNavController,connectViewModel,connectChatViewModel, userViewModel, messageViewModel)
        }
    }
}