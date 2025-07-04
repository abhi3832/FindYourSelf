package com.example.findyourself.view.screens.authScreens.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DisplayProfileIcon(){
    Box(
        modifier = Modifier.shadow(40.dp, CircleShape, spotColor = MaterialTheme.colorScheme.onBackground)
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.Gray) ,
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Picture", tint = Color.White)
    }
}