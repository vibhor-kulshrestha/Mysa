package ai.mysmartassistant.mysa.domain.auth

sealed class LoginNextStep {
    object Home : LoginNextStep()
    object Onboarding : LoginNextStep()
}