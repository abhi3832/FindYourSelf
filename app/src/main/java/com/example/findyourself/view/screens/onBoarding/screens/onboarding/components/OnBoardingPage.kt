package com.example.findyourself.view.screens.onBoarding.screens.onboarding.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findyourself.R
import com.example.findyourself.view.theme.FindYourselfTheme

@Composable
fun OnBoardingPage(
    title : String,
    subtitle : String,
    @DrawableRes imageResourceId: Int,
    imageSize: DpSize = DpSize(250.dp, 250.dp),
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 45.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = subtitle,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(24.dp))

        Icon(
            painter = painterResource(id = imageResourceId),
            contentDescription = null,
            modifier = Modifier.size(imageSize),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun OnBoardingPagePreview() {
    FindYourselfTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnBoardingPage(
                title = "Connect Globally",
                subtitle = "Chat with people from around the world",
                imageResourceId = R.drawable.connection,
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            )
        }
    }
}
