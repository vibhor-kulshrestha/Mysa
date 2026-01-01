package ai.mysmartassistant.mysa.viewmodel.auth

import ai.mysmartassistant.mysa.data.auth.TrueCallerAuthPayload
import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.domain.auth.FetchLoginOptionsUseCase
import ai.mysmartassistant.mysa.domain.auth.LoginIntent
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.domain.auth.LoginNextStep
import ai.mysmartassistant.mysa.ui.auth.LoginUiEvent
import ai.mysmartassistant.mysa.domain.auth.SendOtpUseCase
import ai.mysmartassistant.mysa.domain.auth.VerifyLoginUseCase
import ai.mysmartassistant.mysa.domain.location.CountryDialingCodeResolver
import ai.mysmartassistant.mysa.domain.location.CountryProvider
import ai.mysmartassistant.mysa.ui.auth.LoginMode
import ai.mysmartassistant.mysa.ui.auth.LoginUIState
import ai.mysmartassistant.mysa.ui.auth.PhoneInputState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val fetchLoginOptions: FetchLoginOptionsUseCase,
    private val countryProvider: CountryProvider,
    private val dialingCodeResolver: CountryDialingCodeResolver,
    private val verifyLoginUseCase: VerifyLoginUseCase,
    private val sendOtp: SendOtpUseCase
) : ViewModel() {

    init {
        loadLoginOptions()
    }

    private val _events = MutableSharedFlow<LoginUiEvent>()
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    private val _phoneState = MutableStateFlow(
        createInitialPhoneState()
    )
    val phoneState = _phoneState.asStateFlow()

    private fun createInitialPhoneState(): PhoneInputState {
        val countryIso = countryProvider.getCountryCode()
        val dialingCode =
            dialingCodeResolver.getDialingCode(countryIso)

        return PhoneInputState(
            countryIso = countryIso,
            countryCode = dialingCode
        )
    }

    fun onPhoneChanged(value: String) {
        _phoneState.update { it.copy(phoneNumber = value) }
    }


    private fun loadLoginOptions() {
        viewModelScope.launch {
            when (val result = fetchLoginOptions()) {
                is ApiResult.Success -> {
                    val mediums = result.data
                    _uiState.value = LoginUIState(
                        isLoading = false,
                        loginMediums = mediums,
                        isNotAllowed = mediums.isEmpty()
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = LoginUIState(
                        isLoading = false,
                        error = "Something went wrong"
                    )
                }
            }
        }
    }

    fun onWhatsAppClicked() {
        _uiState.update { it.copy(mode = LoginMode.WhatsAppOtp) }
    }

    fun onMobileClicked() {
        _uiState.update { it.copy(mode = LoginMode.SmsOtp) }
    }

    fun onBackFromOtp() {
        _uiState.update { it.copy(mode = LoginMode.Options) }
    }

    fun onTruecallerPayload(payload: TrueCallerAuthPayload) {
        viewModelScope.launch {
            when(val result = verifyLoginUseCase.execute(
                LoginIntent.Truecaller(payload)
            )) {
                is ApiResult.Error -> {
                    Log.e("OtpViewModel", "exception while onSubmitOtp ${result.error}")
                }
                is ApiResult.Success -> {
                    onLoginSuccess(result.data)
                }
            }
        }
    }

    fun onTruecallerError(error: Throwable?) {
        // update UI state
    }

    fun onPhoneOtpRequested() {
        val state = _phoneState.value
        val uiState = _uiState.value
        if (!state.isValid) return

        val msisdn = "${state.countryCode}${state.phoneNumber}"
        val medium = when (uiState.mode) {
            LoginMode.Options -> null
            LoginMode.SmsOtp -> LoginMedium.SMS
            LoginMode.WhatsAppOtp -> LoginMedium.WHATSAPP
        }
        if (medium == null) return
        _uiState.update { it.copy(isSendingOtp = true) }
        viewModelScope.launch {
            when (val result = sendOtp(
                medium = medium,
                msisdn = msisdn
            )) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isSendingOtp = false) }
                    _events.emit(
                        LoginUiEvent.NavigateToOtp(msisdn, medium)
                    )
                }

                is ApiResult.Error -> {
                    Log.e("LoginViewModel", "exception while sending otp ${result.error}")
                    _uiState.update {
                        it.copy(
                            isSendingOtp = false,
                            error = "Something went Wrong"
                        )
                    }
                }
            }
        }
    }

    fun onLoginSuccess(data: LoginNextStep) {
        viewModelScope.launch {
            when(data) {
                LoginNextStep.Home -> _events.emit(LoginUiEvent.NavigateToHome)
                LoginNextStep.Onboarding -> _events.emit(LoginUiEvent.NavigateToOnboarding)
            }
        }
    }
}