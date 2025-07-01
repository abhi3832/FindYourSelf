package com.example.findyourself.view.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.findyourself.view.screens.onBoarding.screens.onBoardingScreen

fun NavGraphBuilder.onBoardingGraph(
    rootNavController: NavHostController,
) {
    navigation(route = Graphs.ONBOARDING_GRAPH, startDestination = OnBoardingScreens.OnBoardingScreen.route) {
        composable(OnBoardingScreens.OnBoardingScreen.route) {
            onBoardingScreen(rootNavController)
        }
    }
}