package com.example.findyourself.screens.onBoarding.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowCount(pagerState: PagerState, pageCount: Int) {
    Box(modifier = Modifier.background(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.onBackground
    ).padding(8.dp)){
        Text(
            text = " ${pagerState.currentPage + 1} of $pageCount ",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.surface
        )
    }
}