package com.example.findyourself.view.screens.onBoarding.screens.onboarding.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.findyourself.view.theme.FindYourselfTheme

@Composable
fun ShowLoginSignUpButton(
    onClick: () -> Unit,
) {
    Button(
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(60.dp)
            .padding(start = 8.dp, end = 8.dp)

    ) {
        Text("Continue")
    }
}

@Preview
@Composable
private fun ShowLoginSignUpButtonPreview() {
    FindYourselfTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ShowLoginSignUpButton(onClick = {})
        }
    }
}
