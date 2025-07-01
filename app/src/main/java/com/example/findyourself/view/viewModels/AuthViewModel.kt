package com.example.findyourself.view.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findyourself.data.repositories.AuthRepositories
import com.example.findyourself.data.repositories.UserRepository
import com.example.findyourself.model.SignUpRequest
import com.example.findyourself.model.SignUpResponse
import com.example.findyourself.model.UsernameResponse
import com.example.findyourself.model.VerifyOtpResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class OtpUiEvent {
    data class Success(val verificationId: String) : OtpUiEvent()
    data class Error(val message: String) : OtpUiEvent()
}

sealed class VerifyOtpEvent {
    data class Success(val data: VerifyOtpResponse) : VerifyOtpEvent()
    data class NewUser(val data: VerifyOtpResponse) : VerifyOtpEvent()
    data class Error(val message: String) : VerifyOtpEvent()
    data class InValid(val message: String) : VerifyOtpEvent()
    object Idle : VerifyOtpEvent()
}

sealed class CheckUserNameEvent{
    data class Exists(val data: UsernameResponse) : CheckUserNameEvent()
    data class NotExists(val data: UsernameResponse) : CheckUserNameEvent()
    object Loading : CheckUserNameEvent()
    object Idle : CheckUserNameEvent()
    data class Error(val message: String) : CheckUserNameEvent()
}

sealed class SignUpEvent{
    data class SuccessFul(val data: SignUpResponse) : SignUpEvent()
    data class NotSuccessFul(val data: SignUpResponse) : SignUpEvent()
    data class Error(val message: String) : SignUpEvent()
    object Loading : SignUpEvent()
    object Idle : SignUpEvent()

}

enum class AuthState {
     AUTHENTICATED, UNAUTHENTICATED, ONBOARDING
}

class AuthViewModel : ViewModel(), KoinComponent {
    private val authRepo: AuthRepositories by inject()
    private val userRepository: UserRepository by inject()

    private val _authState = MutableSharedFlow<AuthState>()
    val authState: SharedFlow<AuthState> = _authState

    private val _authDone = mutableStateOf(false)
    val authDone: MutableState<Boolean> = _authDone

    private val _onBoarded = mutableStateOf(false)
    val onBoarded : MutableState<Boolean> = _onBoarded



    fun checkAuthStatus() {
        viewModelScope.launch {
            val isOnboardingCompleted = authRepo.isOnboardingCompleted()
            if (isOnboardingCompleted) {
                _onBoarded.value = true
                Log.d("StartDestination", "Onboarding completed !")
                if (authRepo.validateOrRefreshToken()) {
                    _authState.emit(AuthState.AUTHENTICATED)
                    _authDone.value = true
                } else {
                    _authState.emit(AuthState.UNAUTHENTICATED)
                    _authDone.value = false
                }
            } else {
                Log.d("StartDestination", "Onboarding not completed")
                _authState.emit(AuthState.ONBOARDING)
                authRepo.setOnboardingCompleted()
            }
        }
    }



    init{
        checkAuthStatus()
    }

    fun setOnboardingCompleted() {
        authRepo.setOnboardingCompleted()
    }

    fun clearUserData() {
        viewModelScope.launch {
            userRepository.clearAll()
            _authState.emit(AuthState.UNAUTHENTICATED)
        }
    }

    private val _otpEvent = MutableSharedFlow<OtpUiEvent>()
    val otpEvent: SharedFlow<OtpUiEvent> = _otpEvent
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun getOtp(phoneNumber: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = authRepo.sendOtp(phoneNumber)
            _loading.value = false
            result.fold(
                onSuccess = {
                    _otpEvent.emit(OtpUiEvent.Success(it.message))
                },
                onFailure = {
                    _otpEvent.emit(OtpUiEvent.Error(it.message ?: "Unknown error"))
                }
            )
        }
    }

    private val _otpVerificationEvent = MutableSharedFlow<VerifyOtpEvent>()
    val otpVerificationEvent: SharedFlow<VerifyOtpEvent> = _otpVerificationEvent

    fun verifyOtp(otp: String, phoneNumber: String) {
        Log.d("Check123", "Func Triggered ")
        viewModelScope.launch {
            try {
                _loading.value = true
                val result = authRepo.verifyOtp(phoneNumber, otp)
                _loading.value = false

                result.fold(
                    onSuccess = { response ->
                        if (response.isOtpVerified) {
                            if (response.isNewUser) {
                                _otpVerificationEvent.emit(VerifyOtpEvent.NewUser(response))
                            } else {
                                _otpVerificationEvent.emit(VerifyOtpEvent.Success(response))
                            }
                        } else {
                            _otpVerificationEvent.emit(VerifyOtpEvent.InValid("Invalid OTP"))
                        }
                    },
                    onFailure = { error ->
                        _otpVerificationEvent.emit(VerifyOtpEvent.Error("Something went wrong: ${error.message}"))
                    }
                )
            } catch (e: Exception) {
                _otpVerificationEvent.emit(VerifyOtpEvent.Error("Unexpected error: ${e.message}"))
            }
        }
    }

    private val _usernameExists = MutableSharedFlow<CheckUserNameEvent>()
    val usernameExists: SharedFlow<CheckUserNameEvent> = _usernameExists

    fun checkUserName(userName : String) {
        Log.d("Check123", "Check Username Event Triggered")
        viewModelScope.launch {
            try {
                _usernameExists.emit(CheckUserNameEvent.Loading)
                val result = authRepo.checkUsername(userName)
                _usernameExists.emit(CheckUserNameEvent.Idle)

                result.fold(
                    onSuccess = { response ->
                        if (response.exists) {
                            _usernameExists.emit(CheckUserNameEvent.Exists(response))
                        } else {
                            _usernameExists.emit(CheckUserNameEvent.NotExists(response))
                        }
                    },
                    onFailure = { error ->
                        _usernameExists.emit(CheckUserNameEvent.Error("Something went wrong ! ${error.message}"))
                    }
                )
            } catch (e: Exception) {
                _usernameExists.emit(CheckUserNameEvent.Error("Unexpected Error: ${e.message}"))
            }
        }
    }

    private val _signUpEvent = MutableSharedFlow<SignUpEvent>()
    val signUpEvent: SharedFlow<SignUpEvent> = _signUpEvent
    fun signUpEvent(signUpRequest: SignUpRequest){
        Log.d("Check123", "SignUp Event Triggered")
        viewModelScope.launch {
            try{
                _signUpEvent.emit(SignUpEvent.Loading)
                val response = authRepo.signUp(signUpRequest)
                _signUpEvent.emit(SignUpEvent.Idle)
                response.fold(
                    onSuccess = { response->
                        Log.d("Check123", "Response for SignUp Event : $response")
                        if(response.isOtpVerified && response.user != null && response.refreshToken != null && response.accessToken != null){
                            _signUpEvent.emit(SignUpEvent.SuccessFul(response))
                        }else {
                            _signUpEvent.emit(SignUpEvent.NotSuccessFul(response))
                        }
                    },
                    onFailure = { it ->
                        _signUpEvent.emit(SignUpEvent.Error("Something went wrong ! ${it.message}"))
                    }
                )

            }catch (e: Exception){
                _signUpEvent.emit(SignUpEvent.Error("Unexpected Error: ${e.message}"))
            }
        }
    }
}
