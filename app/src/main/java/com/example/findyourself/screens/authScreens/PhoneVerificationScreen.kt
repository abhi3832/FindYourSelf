package com.example.findyourself.screens.authScreens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arpitkatiyarprojects.countrypicker.CountryPickerOutlinedTextField
import com.arpitkatiyarprojects.countrypicker.enums.CountryListDisplayType
import com.arpitkatiyarprojects.countrypicker.utils.CountryPickerDefault
import com.arpitkatiyarprojects.countrypicker.utils.CountryPickerUtils
import com.arpitkatiyarprojects.countrypicker.utils.CountryPickerUtils.getExampleMobileNumber
import com.example.findyourself.navigation.AuthScreens
import com.example.findyourself.screens.authScreens.utils.FullSizedButton
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.OtpUiEvent
import com.example.findyourself.viewModels.UserViewModel

@Composable
fun PhoneVerificationScreen(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {

    val mobileNumber = rememberSaveable { mutableStateOf("") }
    val selectedCountryCode = rememberSaveable{
        mutableStateOf("")
    }

    val selectedCountryDialCode = rememberSaveable{
        mutableStateOf("")
    }
    val isMobileNameValid = remember(mobileNumber.value) { isMobileNumberValid(mobileNumber.value) }
    val context = LocalContext.current
    val isValid = remember { mutableStateOf<Boolean?>(null) }
    val isError = remember { mutableStateOf<Boolean?>(null) }

    val scrollState = rememberScrollState()

    val isLoading by authViewModel.loading.collectAsState()
    val colors =   OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
    )

    LaunchedEffect(Unit) {
        authViewModel.otpEvent.collect { event ->
            Log.d("asdf", "PhoneVerificationScreen: $event")
            when (event) {
                is OtpUiEvent.Success -> {
                    rootNavController.navigate(
                        AuthScreens.OtpScreen.createRoute(
                            mobileNumber.value,
                            selectedCountryDialCode.value
                        )
                    )
                }
                is OtpUiEvent.Error -> {
                    isError.value = false
                }

            }
        }
    }

    val exampleNumber = rememberSaveable{mutableStateOf("")}

    Log.d("asdf", selectedCountryCode.value)
    Log.d("asdf", exampleNumber.value)


    Column(
        modifier = Modifier
            .fillMaxSize().background(MaterialTheme.colorScheme.background)
            .imePadding()
            .scrollable(state = scrollState, orientation = Orientation.Vertical)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {

        WelcomeBackText()
        Spacer(modifier = Modifier.height(8.dp))

        CountryPickerOutlinedTextField(
            defaultCountryCode = "IN",
            mobileNumber = mobileNumber.value,
            onMobileNumberChange = {mobileNumber.value = if (it.length <= exampleNumber.value.length) it else it.take(exampleNumber.value.length)},
            onCountrySelected = {country ->
                selectedCountryCode.value = country.countryCode
                selectedCountryDialCode.value = country.countryPhoneNumberCode
                exampleNumber.value = getExampleMobileNumber(country.countryCode)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
            ),
            label = {Text("Mobile", modifier = Modifier.alpha(0.7f), color = MaterialTheme.colorScheme.onBackground)},
            placeholder = { Text(exampleNumber.value) },
            singleLine = true,
            maxLines = 1,
            supportingText = {
                if (mobileNumber.value.isNotEmpty()) {
                    Text(
                        text = if (!isMobileNameValid) "Use letters Only" else "",
                        color = if (isMobileNameValid) Color.Green else Color.Red
                    )
                }
            },
            trailingIcon = {
                Text(
                    text = "${mobileNumber.value.length}/${exampleNumber.value.length}",
                    fontSize = 12.sp,
                    color = if (mobileNumber.value.length <= 30) Color.Gray else Color.Red,
                )
            },
            isError = mobileNumber.value.isNotEmpty() && !isMobileNameValid,
            countryPickerColors = CountryPickerDefault.colors(
                dropDownIconColor = MaterialTheme.colorScheme.onBackground,
                backIconColor = MaterialTheme.colorScheme.onBackground,
                searchIconColor = MaterialTheme.colorScheme.onBackground,
                cancelIconColor = MaterialTheme.colorScheme.onBackground,
                searchCursorColor = MaterialTheme.colorScheme.onBackground,
                selectedCountryContainerColor = Color.Gray.copy(alpha = 0.22f),
                countriesListContainerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = Modifier.fillMaxWidth(),
            countryListDisplayType = CountryListDisplayType.Dialog
        )

        Spacer(modifier = Modifier.height(16.dp))

        FullSizedButton("Continue", mobileNumber) {
            isValid.value = CountryPickerUtils.isMobileNumberValid(mobileNumber.value, selectedCountryCode.value)
            if (isValid.value == true) {
                val fullPhone = selectedCountryDialCode.value + mobileNumber.value
                authViewModel.getOtp(fullPhone)
                isValid.value = null
            }
        }

        if(isValid.value == false){
            showToast(context, "Invalid Phone Number")
            isValid.value = null
        }
        if(isError.value == false){
            showToast(context, "Unknown Error")
            isError.value = null
        }

        if(isLoading){
            CircularProgressIndicator()
        }



    }

}

@Composable
fun WelcomeBackText(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        Text(
            text = "Verify your number",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun isMobileNumberValid(mobileNumber: String): Boolean {
    val MOBILE_REGEX = Regex("^[0-9]+$")
    return MOBILE_REGEX.matches(mobileNumber)
}


