package com.example.findyourself.screens.authScreens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composeuisuite.ohteepee.OhTeePeeDefaults
import com.composeuisuite.ohteepee.OhTeePeeInput
import com.example.findyourself.navigation.AuthScreens
import com.example.findyourself.navigation.Graphs
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.UserViewModel
import com.example.findyourself.viewModels.VerifyOtpEvent
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OtpScreen(
    rootNavController: NavHostController,
    mobileNumber: String,
    countryCode: String,
) {
    val authViewModel: AuthViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()

    var secondsLeft by remember { mutableStateOf(30) }
    val otpValue = remember { mutableStateOf("") }
    val context = LocalContext.current

    val isValid = remember { mutableStateOf<Boolean?>(null) }
    val isOtpValid = remember { mutableStateOf<Boolean?>(null) }
    val isError = remember { mutableStateOf<Boolean?>(null) }

    val otpState = remember { mutableStateOf(OtpState.None) }

    val scrollState = rememberScrollState()



    LaunchedEffect(secondsLeft) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
    }

    LaunchedEffect(isValid.value) {
        if (isValid.value == false) {
            showToast(context, "Incorrect Otp")
            isValid.value = null
        }
    }

    LaunchedEffect(isError.value) {
        if (isError.value == false) {
            showToast(context, "Error Verifying Number")
            isError.value = null
        }
    }


    LaunchedEffect(Unit) {
        authViewModel.otpVerificationEvent.collect { event ->
            when (event) {
                is VerifyOtpEvent.Success -> {

                    Log.d("Check123", "Otp Verified! Welcome back.")
                    otpState.value = OtpState.None
                    val data = event.data
                    val accessToken = data.accessToken
                    val refreshToken = data.refreshToken
                    val user = data.user

                    if( accessToken != null && refreshToken != null){
                        userViewModel.saveUserSession(
                            user = user,
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )

                        userViewModel.loadUser()

                        rootNavController.navigate(Graphs.MAIN_NAV_GRAPH){
                            popUpTo(Graphs.AUTH_GRAPH){
                                inclusive = true
                            }
                        }
                    }
                }
                is VerifyOtpEvent.NewUser -> {
                    Log.d("Check123", "Otp Verified! New user flow.")
                    rootNavController.navigate(AuthScreens.EnterCredentials.createRoute(countryCode+mobileNumber))
                    otpState.value = OtpState.None
                }
                is VerifyOtpEvent.Error -> {
                    isError.value = false
                    Log.d("Check123", "Otp Error: ${event.message}")
                    otpState.value = OtpState.Error
                    otpValue.value = ""
                }
                is VerifyOtpEvent.Idle -> {
                    otpState.value = OtpState.None
                }
                is VerifyOtpEvent.InValid -> {
                    Log.d("Check123", "Otp Invalid: ${event.message}")
                    isValid.value = false
                    otpState.value = OtpState.Error
                    otpValue.value = ""
                }
            }
        }
    }

    BackHandler {
        Log.d("Check123","Back Button Pressed !")
        rootNavController.popBackStack()
    }

    Column(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background).imePadding()
        .scrollable(state = scrollState, orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        TextHeading(mobileNumber, countryCode)
        OtpBoxes(otpValue, mobileNumber, authViewModel, countryCode, isOtpValid, otpState)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (secondsLeft > 0) "Resend OTP in $secondsLeft seconds" else "Resend OTP",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
                .alpha(if (secondsLeft > 0) 0.7f else 1f)
                .clickable {
                   if(secondsLeft == 0){
                       val fullPhone = countryCode + mobileNumber
                       authViewModel.getOtp(fullPhone)
                       secondsLeft = 30
                   }
                }
        )

        Text(
            text = "Wrong Number?",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
                .clickable {
                    rootNavController.popBackStack()
                }
        )

    }
}

enum class OtpState{Loading, Error, None}

@Composable
fun OtpBoxes(
    otpValue: MutableState<String>,
    mobileNumber: String,
    authViewModel: AuthViewModel,
    countryCode: String,
    isOtpValid: MutableState<Boolean?>,
    otpState: MutableState<OtpState>
) {

    LaunchedEffect(otpValue.value) {
        if (otpState.value == OtpState.Error && otpValue.value.length < 6) {
            otpState.value = OtpState.None
        }
    }

    Log.d("Check123","Otp State Value : ${otpState.value}")



    val defaultCellConfig = OhTeePeeDefaults.cellConfiguration(
        borderColor = Color.LightGray,
        borderWidth = 1.dp,
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(
            color = Color.Black
        ),

        )

    OhTeePeeInput(
        value = otpValue.value,
        onValueChange = { newValue, isValid ->
            otpValue.value = newValue

            val cleanOtp = newValue.filter { it.isDigit() }

            if (cleanOtp.length == 6) {
                Log.d("Check123", "Reached Full Length : $cleanOtp")
                otpState.value = OtpState.Loading
                authViewModel.verifyOtp(
                    otp = cleanOtp,
                    phoneNumber = countryCode + mobileNumber,
                )
               // otpState.value = OtpState.None
            } else {
                otpState.value = OtpState.None
            }
        },
        configurations = OhTeePeeDefaults.inputConfiguration(
            cellsCount = 6,
            emptyCellConfig = defaultCellConfig,
            cellModifier = Modifier.size(48.dp),
            activeCellConfig = defaultCellConfig.copy(
                borderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                borderWidth = 2.dp
            ),
            errorCellConfig = defaultCellConfig.copy(
                borderColor = Color.Red,
                borderWidth = 2.dp
            ),
        ),

        enabled = otpState.value != OtpState.Loading,
        isValueInvalid = otpState.value == OtpState.Error
    )
}

@Composable
fun TextHeading(mobileNumber: String, countryCode: String) {
    Column(modifier = Modifier.fillMaxWidth(0.9f).padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(
            text = "Verify Your Phone Number",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = "We've sent a 6-digit OTP to your mobile number $countryCode $mobileNumber",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp).alpha(0.7f)
        )

    }

}
