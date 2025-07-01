package com.example.findyourself.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.findyourself.view.screens.bottomNavigatoinScreens.*

@Composable
fun MainNavGraph(
    mainNavController: NavHostController,
    rootNavController: NavHostController,
) {
    NavHost(
        navController = mainNavController,
        route = Graphs.MAIN_NAV_GRAPH,
        startDestination = MainNavScreens.ChatScreen.route
    ) {
        composable(MainNavScreens.ChatScreen.route) { HomeScreen() }
        composable(MainNavScreens.ConnectScreen.route) { ConnectScreen(rootNavController) }
        composable(MainNavScreens.ProfileScreen.route) { ProfileScreen() }
        composable(MainNavScreens.HistoryScreen.route) { HistoryScreen() }
        composable(MainNavScreens.NotificationScreen.route) { InboxScreen() }
    }
}