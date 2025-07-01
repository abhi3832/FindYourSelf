package com.example.findyourself.view.screens.authScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.findyourself.view.navigation.Graphs
import com.example.findyourself.model.SignUpRequest
import com.example.findyourself.view.screens.authScreens.utils.DisplayProfileIcon
import com.example.findyourself.view.screens.authScreens.utils.Gender
import com.example.findyourself.view.screens.authScreens.utils.GenderSelection
import com.example.findyourself.view.viewModels.AuthViewModel
import com.example.findyourself.view.viewModels.CheckUserNameEvent
import com.example.findyourself.view.viewModels.SignUpEvent
import com.example.findyourself.view.viewModels.UserViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

//rootNavController: NavHostController, authViewModel: AuthViewModel,

@Composable
fun SignUpScreen(
    rootNavController: NavHostController,
    fullMobileNumber: String,
) {
    val authViewModel: AuthViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()

    val fullName = remember { mutableStateOf("") }
    val userName = remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    val aboutYourself = remember { mutableStateOf("") }
    val age = remember { mutableIntStateOf(18) }
    val isUserNameValid = remember(userName.value) { isUsernameValid(userName.value) }
    val isFullNameValid = remember(fullName.value) { isFullNameGrammaticallyValid(fullName.value) }
    val doesUserNameExists = remember { mutableStateOf(false) }
    val userNameCheckLoading = remember { mutableStateOf(false) }
    val userNameCheckError = remember { mutableStateOf(false) }
    val context = LocalContext.current

    //val enableContinueButton = remember() {mutableStateOf(false)}
    var lastCheckedUserName by remember { mutableStateOf("") }
    val showLoadingIndicator = remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    LaunchedEffect(userName.value) {
        if (isUsernameValid(userName.value) && userName.value != lastCheckedUserName) {
            delay(500) // debounce
            lastCheckedUserName = userName.value
            authViewModel.checkUserName(userName.value)
        }
    }

    val colors =   OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
    )

    // for checking username exists or not
    LaunchedEffect(Unit){
        authViewModel.usernameExists.collect { event ->
            when(event){
                is CheckUserNameEvent.Exists ->{
                    doesUserNameExists.value = true
                }
                is CheckUserNameEvent.NotExists ->{
                    doesUserNameExists.value = false
                }
                is CheckUserNameEvent.Loading ->{
                    userNameCheckLoading.value = true
                }
                is CheckUserNameEvent.Idle ->{
                    userNameCheckLoading.value = false
                }
                is CheckUserNameEvent.Error ->{
                    userNameCheckError.value = true
                }
            }
        }
    }

    // for completing the registration process
    LaunchedEffect(Unit){
        authViewModel.signUpEvent.collect { event ->

            when(event){
                is SignUpEvent.SuccessFul ->{
                    val data = event.data
                    val user = data.user
                    val accessToken = data.accessToken
                    val refreshToken = data.refreshToken

                    if(user != null && accessToken != null && refreshToken != null){
                        userViewModel.saveUserSession(user,accessToken,refreshToken)
                        userViewModel.loadUser()
                        rootNavController.navigate(Graphs.MAIN_NAV_GRAPH){
                            popUpTo(Graphs.AUTH_GRAPH){
                                inclusive = true
                            }
                        }
                    }
                    else{
                        showToast(context, "Error, Couldn't Complete Registration Process")
                    }
                }
                is SignUpEvent.NotSuccessFul ->{
                    showToast(context, "Couldn't Complete Registration Process")
                }
                is SignUpEvent.Error ->{
                    showToast(context, "Error Occurred")
                }
                is SignUpEvent.Loading ->{
                    showLoadingIndicator.value = true
                }
                is SignUpEvent.Idle ->{
                    showLoadingIndicator.value = true
                }

            }

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize().background(MaterialTheme.colorScheme.background).imePadding()
            .statusBarsPadding().navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome",
            fontSize = 30.sp,
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))
        DisplayProfileIcon()
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName.value,
            onValueChange = { fullName.value = if (it.length <= 30) it else it.take(30)},
            label = { Text("Full Name", modifier = Modifier.alpha(0.7f), color = MaterialTheme.colorScheme.onBackground) },
            placeholder = { Text("Enter your Full Name") },
            isError = fullName.value.isNotEmpty() && !isFullNameValid,
            supportingText = {
                if (fullName.value.isNotEmpty()) {
                    Text(
                        text = if (!isFullNameValid) "5–20 characters. Use letters, numbers, ' . ' , ' _ ' , ' - ' " else "",
                        color = if (isFullNameValid) Color.Green else Color.Red
                    )
                }
            },
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            trailingIcon = {
                Text(
                    text = "${fullName.value.length}/30",
                    fontSize = 12.sp,
                    color = if (fullName.value.length <= 30) Color.Gray else Color.Red,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = colors
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userName.value,
            onValueChange = {
                val input = if (it.length <= 20) it else it.take(20)
                userName.value = input
            },
            label = { Text("Username", modifier = Modifier.alpha(0.7f), color = MaterialTheme.colorScheme.onBackground) },
            placeholder = { Text("Enter username") },
            isError = userName.value.isNotEmpty() && !isUserNameValid,
            supportingText = {
                when {
                    userName.value.isNotEmpty() && !isUserNameValid -> {
                        Text(text = "5–20 characters. Use letters, numbers, ' . '  , ' _ '  , ' - '", color = Color.Red)
                    }
                    userNameCheckLoading.value -> {
                        Text("Checking username...", color = Color.Gray)
                    }
                    doesUserNameExists.value -> {
                        Text("Username already taken - ${userName.value}", color = Color.Red)
                    }
                    userName.value.isNotEmpty() && isUserNameValid && !doesUserNameExists.value -> {
                        Text("Username is available -  ${userName.value}", color = Color.Green)
                    }
                    else -> null
                }

            },
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            trailingIcon = {
                Text(
                    text = "${userName.value.length}/20",
                    fontSize = 12.sp,
                    color = if (userName.value.length <= 30) Color.Gray else Color.Red,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = colors
        )


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fullMobileNumber,
            onValueChange = { },
            label = { },
            placeholder = {  },
            singleLine = true,
            maxLines = 1,
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PhoneAndroid,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        GenderSelection(selectedGender) { newGender -> selectedGender = newGender }

        AgeSlider(age = age.value, onAgeChange = { age.value = it })


        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center)
        {

            val maxWords = 100

            val currentWordCount = aboutYourself.value.trim()
                .split("\\s+".toRegex()).filter { it.isNotBlank() }.size

            TextField(
                value = aboutYourself.value,
                onValueChange = {
                    val wordCount = it.trim().split("\\s+".toRegex()).filter { word -> word.isNotBlank() }.size
                    if (wordCount <= maxWords) {
                        aboutYourself.value = it
                    }
                },
                label = {
                    Text(
                        text = "$currentWordCount/$maxWords words",
                        fontSize = 12.sp,
                        color = if (currentWordCount < maxWords) Color.Gray else Color.Red,
                        modifier = Modifier.align(Alignment.End)
                    )
                },
                placeholder = { Text("Tell us about yourself") },
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth().height(100.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .clip(shape = RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.22f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.22f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val continueButtonEnabled by remember(
            isUserNameValid,
            isFullNameValid,
            doesUserNameExists.value,
            selectedGender,
            age.value,
            aboutYourself.value
        ) {
            derivedStateOf {
                isUserNameValid &&
                    isFullNameValid &&
                    !doesUserNameExists.value &&
                    selectedGender != null &&
                    age.value >= 15 &&
                    aboutYourself.value.trim().isNotEmpty()
            }
        }

        val onClick = {
            if(continueButtonEnabled){
                Log.d("Check123", "Continue Button Clicked, request sent to server")
                authViewModel.signUpEvent(
                    signUpRequest = SignUpRequest(
                        fullName = fullName.value,
                        username = userName.value,
                        phoneNumber = fullMobileNumber,
                        gender = when(selectedGender){
                            Gender.Male -> "male"
                            Gender.Female -> "female"
                            Gender.Others -> "others"
                            else -> "others"
                        },
                        profilePhotoUrl = null,
                        age = age.value,
                        about = aboutYourself.value.trim()
                    )
                )
            }
        }

        Button(
            onClick = { onClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp).alpha(if(continueButtonEnabled) 1f else 0.6f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center)
            {
                Text("Continue",
                    color = MaterialTheme.colorScheme.background)

                if(showLoadingIndicator.value){
                    Spacer(modifier = Modifier.width(8.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.Red
                    )
                }

            }
        }

    }

}

@Composable
fun AgeSlider(age: Int, onAgeChange: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Age: $age",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Slider(
            value = age.toFloat(),
            onValueChange = { newValue -> onAgeChange(newValue.toInt()) },
            valueRange = 15f..100f,
            steps = 85, // 100 - 15 = 85
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

fun isUsernameValid(username: String): Boolean {
    val USERNAME_REGEX = Regex("^[a-zA-Z0-9](?!.*[_.-]{2})[a-zA-Z0-9._-]{3,18}[a-zA-Z0-9]$")
    return username.length >= 5 && username.length <= 20 && USERNAME_REGEX.matches(username.trim())
}

fun isFullNameGrammaticallyValid(fullName: String): Boolean {
    val FULL_NAME_REGEX = Regex("^[A-Za-z]+( [A-Za-z]+)*$")
    return fullName.length <= 50 && FULL_NAME_REGEX.matches(fullName.trim())
}



