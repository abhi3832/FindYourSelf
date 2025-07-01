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
    startDestination: String,
) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController, route = Graphs.ROOT_GRAPH,
        startDestination = startDestination
    ) {
        authGraph(rootNavController)
        onBoardingGraph(rootNavController)
        composable(Graphs.MAIN_NAV_GRAPH) { MainScreen(rootNavController) }
        composable(
            UserProfileDetailScreen.ProfileDetailScreen.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserProfileScreenForRandomChat(userId)
        }
        composable(
            route = ConnectChatDetailScreen.DetailScreen.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ConnectChatScreen(rootNavController, chatId)
        }
    }
}