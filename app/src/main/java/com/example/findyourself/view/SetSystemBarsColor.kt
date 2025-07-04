package com.example.findyourself.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController


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