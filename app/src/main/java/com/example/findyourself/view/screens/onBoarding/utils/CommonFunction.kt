package com.example.findyourself.view.screens.onBoarding.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findyourself.R
import com.example.findyourself.view.screens.onBoarding.utils.LoadImage

@Composable
fun CommonFunction
(
    text1 : String,
    text2 : String,
    @DrawableRes id : Int,
    surfaceModifier: Modifier
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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
                text = text1,
                fontSize = 45.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = text2,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(24.dp))
        LoadImage(
            id = id,
            surfaceModifier = surfaceModifier
        )

    }
}
