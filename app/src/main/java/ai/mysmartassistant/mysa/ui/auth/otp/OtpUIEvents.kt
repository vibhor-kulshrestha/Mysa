package ai.mysmartassistant.mysa.ui.auth.otp

import ai.mysmartassistant.mysa.domain.auth.LoginMedium

sealed interface OtpUIEvents {
    object NavigateToHome : OtpUIEvents
    object NavigateToOnboarding : OtpUIEvents
}