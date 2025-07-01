package com.example.findyourself.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.findyourself.screens.authScreens.PhoneVerificationScreen
import com.example.findyourself.screens.authScreens.OtpScreen
import com.example.findyourself.screens.authScreens.SignUpScreen
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.UserViewModel

fun NavGraphBuilder.authGraph(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {


    navigation(route = Graphs.AUTHGRAPH, startDestination = AuthScreens.PhoneVerification.route){
        composable(AuthScreens.PhoneVerification.route){
            PhoneVerificationScreen(rootNavController, authViewModel, userViewModel)
        }
        composable(AuthScreens.EnterCredentials.route,
            arguments = listOf(navArgument("fullMobileNumber") { type = NavType.StringType }))
        { backStackEntry ->
            val fullMobileNumber = backStackEntry.arguments?.getString("fullMobileNumber") ?: ""
            SignUpScreen(rootNavController,authViewModel, fullMobileNumber, userViewModel)
        }

        composable(
            route = AuthScreens.OtpScreen.route,
            arguments = listOf(navArgument("mobileNumber") { type = NavType.StringType },
                navArgument("countryCode") { type = NavType.StringType })
        ) { backStackEntry ->
            val mobileNumber = backStackEntry.arguments?.getString("mobileNumber") ?: ""
            val countryCode = backStackEntry.arguments?.getString("countryCode") ?: ""
            OtpScreen(rootNavController, authViewModel, mobileNumber, countryCode, userViewModel)
        }
    }
}