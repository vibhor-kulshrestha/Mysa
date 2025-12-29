package ai.mysmartassistant.mysa.ui.auth.otp

import ai.mysmartassistant.mysa.domain.auth.LoginMedium

data class OtpUiState(
    val phoneNumber: String,
    val medium: LoginMedium,
    val otp: String = "",
    val isSubmitting: Boolean = false,
    val resendSeconds: Int = 60,
    val canResend: Boolean = false,
    val error: String? = null
)
