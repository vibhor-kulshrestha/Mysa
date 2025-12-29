package ai.mysmartassistant.mysa.domain.auth

sealed interface LoginUiEvent {
    data class NavigateToOtp(val phone: String,val medium : LoginMedium) : LoginUiEvent
    object NavigateToHome : LoginUiEvent
    object NavigateToOnboarding : LoginUiEvent
}