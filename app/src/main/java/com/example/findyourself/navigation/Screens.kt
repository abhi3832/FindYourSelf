package com.example.findyourself.navigation

import com.example.findyourself.dataClasses.Country


object Graphs{

    const val ONBOARDINGGRAPH = "onBoardingGraph"
    const val ROOTGRAPH = "rootGraph"
    const val AUTHGRAPH = "authGraph"
    const val MAINNAVGRAPH = "mainNavGraph"
}

sealed class AuthScreens(val route : String){
    object PhoneVerification : AuthScreens("phoneVerification")
    object EnterCredentials : AuthScreens("enterCredentials/{fullMobileNumber}"){
        fun createRoute(fullMobileNumber: String): String {
            return "enterCredentials/$fullMobileNumber"
        }
    }
    object OtpScreen : AuthScreens("otpScreen/{mobileNumber}/{countryCode}"){
        fun createRoute(mobileNumber: String, countryCode : String): String {
            return "otpScreen/$mobileNumber/$countryCode"
        }
    }

}

sealed class OnBoardingScreens(val route : String){
    object OnBoardingScreen : OnBoardingScreens("onBoardingScreen")
}

sealed class MainNavScreens(val route: String) {
    object ChatScreen : MainNavScreens("chats")
    object HistoryScreen : MainNavScreens("history")
    object ConnectScreen : MainNavScreens("connect")
    object NotificationScreen : MainNavScreens("notification")
    object ProfileScreen : MainNavScreens("profile")
}

sealed class UserProfileDetailScreen(val route : String){
    object ProfileDetailScreen : UserProfileDetailScreen("userProfileScreen/{userId}"){
        fun createRoute(userId: String): String {
            return "userProfileScreen/$userId"
        }
    }
}

sealed class ConnectChatDetailScreen(val route : String){

    object DetailScreen : ConnectChatDetailScreen("connectChatDetailScreen/{chatId}") {
        fun createRoute(chatId: String): String {
            return "connectChatDetailScreen/$chatId"
        }
    }
}

sealed class EndedChatDetailScreen(val route : String){

    object DetailScreen : EndedChatDetailScreen("endedChatDetailScreen/{chatId}") {
        fun createRoute(chatId: String): String {
            return "endedChatDetailScreen/$chatId"
        }
    }
}

sealed class FriendChatDetailScreen(val route : String){

    object DetailScreen : FriendChatDetailScreen("friendChatDetailScreen/{chatId}") {
        fun createRoute(chatId: String): String {
            return "friendChatDetailScreen/$chatId"
        }
    }
}




