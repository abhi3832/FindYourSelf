package com.example.findyourself.view.screens.onBoarding.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findyourself.view.theme.FindYourselfTheme

@Composable
fun OnBoardingPageIndicator(
    currentPage: Int,
    pageCount: Int,
) {
    Text(
        text = "${currentPage + 1} of $pageCount",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .wrapContentSize()
            .background(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            .padding(8.dp)
    )
}

@Preview
@Composable
private fun OnBoardingPageIndicatorPreview() {
    FindYourselfTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnBoardingPageIndicator(0, 3)
        }
    }
}