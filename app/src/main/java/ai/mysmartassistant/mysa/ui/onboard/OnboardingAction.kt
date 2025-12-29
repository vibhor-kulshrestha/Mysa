package ai.mysmartassistant.mysa.ui.onboard

import ai.mysmartassistant.mysa.ui.Gender

sealed interface OnboardingAction {

    data class FirstNameChanged(val value: String) : OnboardingAction
    data class LastNameChanged(val value: String) : OnboardingAction
    data class GenderChanged(val gender: Gender) : OnboardingAction

    object Submit : OnboardingAction
}