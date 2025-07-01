package com.example.findyourself.screens.onBoarding.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadImage(
    @DrawableRes id : Int,
    surfaceModifier: Modifier
)
{
    Icon(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = surfaceModifier,
        tint = MaterialTheme.colorScheme.onSurface
    )

}
