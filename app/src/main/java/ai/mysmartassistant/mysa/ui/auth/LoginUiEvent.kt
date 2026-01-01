package ai.mysmartassistant.mysa.ui.auth

import ai.mysmartassistant.mysa.domain.auth.LoginMedium

sealed interface LoginUiEvent {
    data class NavigateToOtp(val phone: String,val medium : LoginMedium) : LoginUiEvent
    object NavigateToHome : LoginUiEvent
    object NavigateToOnboarding : LoginUiEvent
}