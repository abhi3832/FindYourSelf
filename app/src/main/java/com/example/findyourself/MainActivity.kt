package com.example.findyourself

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.findyourself.navigation.Graphs
import com.example.findyourself.navigation.RootNavGraph
import com.example.findyourself.screens.onBoarding.utils.SetSystemBarsColor
import com.example.findyourself.ui.theme.FindYourselfTheme
import com.example.findyourself.viewModels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.viewmodel.koinViewModel


class MainActivity: ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        //TODO splash screen that cheks auth state
        super.onCreate(savedInstanceState)
        setContent {
            FindYourselfTheme {
                SetSystemBarsColor()
                RootNavGraph(
                    startDestination = getStartDestination(authViewModel)
                )
            }
        }
    }
}

//todo move this to splash screen
private fun getStartDestination(authViewModel: AuthViewModel): String {
    return when {
        !authViewModel.onBoarded.value -> Graphs.ONBOARDING_GRAPH
        !authViewModel.authDone.value -> Graphs.AUTH_GRAPH
        else -> Graphs.MAIN_NAV_GRAPH
    }
}


