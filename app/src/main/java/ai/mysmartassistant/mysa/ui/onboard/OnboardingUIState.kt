package ai.mysmartassistant.mysa.ui.onboard

import ai.mysmartassistant.mysa.ui.Gender

data class OnboardingUIState(
    val firstName: String = "",
    val lastName: String = "",
    val gender: Gender? = null,
    val language: String = "en",
    val isSubmitting: Boolean = false,
    val error: String? = null
)