package com.example.findyourself

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.findyourself.navigation.RootNavGraph
import com.example.findyourself.ui.theme.FindYourselfTheme
import com.example.findyourself.viewModels.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import javax.inject.Inject
import androidx.lifecycle.ViewModelProvider
import com.example.findyourself.navigation.Graphs
import com.example.findyourself.viewModels.AuthState
import com.example.findyourself.viewModels.ConnectChatViewModel
import com.example.findyourself.viewModels.ConnectViewModel
import com.example.findyourself.viewModels.MessageViewModel
import com.example.findyourself.viewModels.UserViewModel


class MainActivity : ComponentActivity() {

    lateinit var authViewModel: AuthViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var connectViewModel: ConnectViewModel
    lateinit var connectChatViewModel : ConnectChatViewModel
    lateinit var messageViewModel: MessageViewModel

    @Inject
    lateinit var mainViewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        (application as FindYourSelfApplication).applicationLevelComponent.inject(this)
        authViewModel = ViewModelProvider(this,mainViewModelFactory)[AuthViewModel :: class.java]
        userViewModel = ViewModelProvider(this,mainViewModelFactory)[UserViewModel :: class.java]
        connectViewModel = ViewModelProvider(this,mainViewModelFactory)[ConnectViewModel :: class.java]
        connectChatViewModel = ViewModelProvider(this,mainViewModelFactory)[ConnectChatViewModel :: class.java]
        messageViewModel = ViewModelProvider(this,mainViewModelFactory)[MessageViewModel :: class.java]



        setContent {
            FindYourselfTheme {
                SetSystemBarsColor()

                RootNavGraph(authViewModel, userViewModel,connectViewModel,connectChatViewModel, messageViewModel )
            }
        }
    }
}

@Composable
fun checkAuthStatus(authViewModel: AuthViewModel): String {
    val authState by authViewModel.authState.collectAsState(initial = AuthState.ONBOARDING)

    return when (authState) {
        AuthState.AUTHENTICATED -> Graphs.MAINNAVGRAPH
        AuthState.UNAUTHENTICATED -> Graphs.AUTHGRAPH
        AuthState.ONBOARDING -> Graphs.ONBOARDINGGRAPH
    }
}


@Composable
fun SetSystemBarsColor() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = isSystemInDarkTheme() // false for dark mode

    val backGround = MaterialTheme.colorScheme.background

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = backGround,
            darkIcons = !useDarkIcons
        )
        systemUiController.setStatusBarColor(
            color = backGround,
            darkIcons = !useDarkIcons
        )
    }
}


