package com.example.findyourself.view.screens.onBoarding.screens.onboarding

import androidx.annotation.DrawableRes
import com.example.findyourself.R

data class OnBoardingScreenState(
    val pages: List<Page> = listOf(
        Page(
            title = "Connect Globally",
            description = "Chat with people from around the world",
            imageResourceId = R.drawable.connection,
        ),
        Page(
            title = "Safe & Anonymous",
            description = "Your privacy is our priority",
            imageResourceId = R.drawable.privacy_policy,
        ),
        Page(
            title = "Find your tribe",
            description = "Connect through shared interest",
            imageResourceId = R.drawable.common_interest,
        ),
    ),
) {
    data class Page(
        val title: String,
        val description: String,
        @DrawableRes val imageResourceId: Int
    )
}