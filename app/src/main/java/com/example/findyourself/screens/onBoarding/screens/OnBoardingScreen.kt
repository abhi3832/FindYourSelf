package com.example.findyourself.screens.onBoarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.findyourself.R
import com.example.findyourself.screens.onBoarding.utils.CommonFunction
import com.example.findyourself.screens.onBoarding.utils.ShowCount
import com.example.findyourself.screens.onBoarding.utils.ShowLoginSignUpButton
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun onBoardingScreen(rootNavController: NavHostController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {

    val pagerState = rememberPagerState()
    val pageCount = 3

    Column(
        modifier = Modifier
            .fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding().navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        HorizontalPager(
            count = pageCount,
            modifier = Modifier.weight(2f).fillMaxWidth(),
            state = pagerState
        ){ page ->
            when(page){
                0 -> CommonFunction(
                    text1 = "Connect Globally",
                    text2 = "Chat with people from around the world",
                    id = R.drawable.connection,
                    surfaceModifier = Modifier.width(250.dp).height(250.dp)
                )
                1 -> CommonFunction(
                    text1 = "Safe & Anonymous",
                    text2 = "Your privacy is our priority",
                    id = R.drawable.privacy_policy,
                    surfaceModifier = Modifier.width(250.dp).height(250.dp)
                )
                2 -> CommonFunction(
                    text1 = "Find your tribe",
                    text2 = "Connect through shared interest",
                    id = R.drawable.common_interest,
                    surfaceModifier = Modifier.width(250.dp).height(250.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ShowCount(pagerState, pageCount)

        Spacer(modifier = Modifier.height(16.dp))

        ShowLoginSignUpButton(rootNavController,authViewModel,userViewModel)

        Spacer(modifier = Modifier.height(200.dp))
    }
}
