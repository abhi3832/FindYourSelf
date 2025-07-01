package com.example.findyourself.screens.onBoarding.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.findyourself.navigation.Graphs
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.UserViewModel

@Composable
fun ShowLoginSignUpButton(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    Row(modifier = Modifier.fillMaxWidth().height(60.dp).padding(start = 8.dp, end = 8.dp), horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Button(
            shape = RoundedCornerShape(16.dp),
            onClick = {
               // authViewModel.setOnboardingCompleted()
                rootNavController.navigate(Graphs.AUTH_GRAPH){
                    popUpTo(Graphs.ONBOARDING_GRAPH){inclusive = true}
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxSize()

        ){
            Text("Continue")
        }
    }
}
