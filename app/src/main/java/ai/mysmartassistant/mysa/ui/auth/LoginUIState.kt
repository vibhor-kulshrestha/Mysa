package ai.mysmartassistant.mysa.ui.auth

import ai.mysmartassistant.mysa.domain.auth.LoginMedium

data class LoginUIState(
    val isLoading: Boolean = true,
    val isSendingOtp: Boolean = false,
    val loginMediums: List<LoginMedium> = emptyList(),
    val isNotAllowed: Boolean = false,
    val mode: LoginMode = LoginMode.Options,
    val error: String? = null
)
