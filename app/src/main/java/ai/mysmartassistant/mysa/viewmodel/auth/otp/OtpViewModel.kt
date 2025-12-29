package ai.mysmartassistant.mysa.viewmodel.auth.otp

import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.domain.auth.LoginIntent.*
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.domain.auth.SendOtpUseCase
import ai.mysmartassistant.mysa.domain.auth.VerifyLoginUseCase
import ai.mysmartassistant.mysa.ui.auth.otp.OtpUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val verifyLoginUseCase: VerifyLoginUseCase,
    private val sendOtp: SendOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OtpUiState(phoneNumber = "", medium = LoginMedium.SMS)
    )
    val uiState = _uiState.asStateFlow()

    fun init(phoneNumber: String, medium: LoginMedium) {
        _uiState.update { it.copy(phoneNumber = phoneNumber, medium = medium) }
        startResendTimer()
    }

    fun onOtpChanged(value: String) {
        if (value.length <= 4 && value.all { it.isDigit() }) {
            _uiState.update { it.copy(otp = value) }
        }
    }

    fun onOtpAutoFilled(code: String) {
        _uiState.update { it.copy(otp = code) }
    }

    fun onSubmitOtp() {
        val state = _uiState.value
        if (state.otp.length != 4) {
            _uiState.update { it.copy(isSubmitting = false, error = "incorrect otp") }
            return
        }
        _uiState.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            when(val result = verifyLoginUseCase.execute(
                Phone(
                    state.phoneNumber,
                    state.otp,
                    state.medium
                )
            )) {
                is ApiResult.Error -> {
                    Log.e("OtpViewModel", "exception while onSubmitOtp ${result.error}")
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            error = "Something went wrong. Please try again later"
                        )
                    }
                }
                is ApiResult.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            error = null
                        )
                    }

                }
            }
        }
    }

    fun onResendOtp() {
        viewModelScope.launch {
            try {
                val value = _uiState.value
                when (val result = sendOtp(
                    medium = value.medium,
                    msisdn = value.phoneNumber
                )) {
                    is ApiResult.Success -> Log.e("LoginViewModel", "resend otp succeeded")
                    is ApiResult.Error -> Log.e(
                        "LoginViewModel",
                        "exception while sending otp ${result.error}"
                    )
                }
            } catch (e: Exception) {
                Log.e("OtpViewModel", "exception while re-sending otp", e)
            }
        }
        startResendTimer()
    }

    private fun startResendTimer() {
        viewModelScope.launch {
            for (i in 60 downTo 0) {
                _uiState.update {
                    it.copy(
                        resendSeconds = i,
                        canResend = i == 0
                    )
                }
                delay(1_000)
            }
        }
    }
}