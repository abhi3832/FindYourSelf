package com.example.findyourself

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.findyourself.view.navigation.Graphs
import com.example.findyourself.view.navigation.RootNavGraph
import com.example.findyourself.view.screens.onBoarding.utils.SetSystemBarsColor
import com.example.findyourself.view.theme.FindYourselfTheme
import com.example.findyourself.view.viewModels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


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


