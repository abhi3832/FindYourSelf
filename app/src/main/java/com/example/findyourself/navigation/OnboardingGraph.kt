package com.example.findyourself.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.findyourself.screens.onBoarding.screens.onBoardingScreen
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.UserViewModel

fun NavGraphBuilder.onBoardingGraph(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    navigation(route = Graphs.ONBOARDINGGRAPH, startDestination = OnBoardingScreens.OnBoardingScreen.route){
        composable(OnBoardingScreens.OnBoardingScreen.route){
            onBoardingScreen(rootNavController,authViewModel,userViewModel)
        }

    }
}