package com.example.findyourself.view.screens.onBoarding.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.findyourself.view.navigation.Graphs
import com.example.findyourself.view.screens.onBoarding.screens.onboarding.components.OnBoardingPage
import com.example.findyourself.view.screens.onBoarding.screens.onboarding.components.OnBoardingPageIndicator
import com.example.findyourself.view.screens.onBoarding.screens.onboarding.components.ShowLoginSignUpButton
import com.example.findyourself.view.theme.FindYourselfTheme

@Composable
fun OnBoardingScreen(rootNavController: NavHostController) {
    OnBoardingScreenContent(
        onShowLoginClicked = {
            rootNavController.navigate(Graphs.AUTH_GRAPH) {
                popUpTo(Graphs.ONBOARDING_GRAPH) { inclusive = true }
            }
        }
    )
}

@Composable
private fun OnBoardingScreenContent(
    state: OnBoardingScreenState = OnBoardingScreenState(),
    onShowLoginClicked: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { state.pages.size })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) { pageIndex ->
            OnBoardingPage(
                title = state.pages[pageIndex].title,
                subtitle = state.pages[pageIndex].description,
                imageResourceId = state.pages[pageIndex].imageResourceId,
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OnBoardingPageIndicator(
            currentPage = pagerState.currentPage,
            pageCount = pagerState.pageCount
        )

        Spacer(modifier = Modifier.height(16.dp))

        ShowLoginSignUpButton(
            onClick = onShowLoginClicked
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview
@Composable
private fun OnBoardingScreenContentPreview() {
    FindYourselfTheme {
        OnBoardingScreenContent(
            onShowLoginClicked = {},
        )
    }
}
