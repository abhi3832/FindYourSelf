package com.example.findyourself.view.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.findyourself.view.screens.onBoarding.screens.onboarding.OnBoardingScreen

fun NavGraphBuilder.onBoardingGraph(
    rootNavController: NavHostController,
) {
    navigation(route = Graphs.ONBOARDING_GRAPH, startDestination = OnBoardingScreens.OnBoardingScreen.route) {
        composable(OnBoardingScreens.OnBoardingScreen.route) {
            OnBoardingScreen(rootNavController)
        }
    }
}