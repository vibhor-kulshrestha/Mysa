package ai.mysmartassistant.mysa.ui.auth

sealed class LoginMode {
    object Options : LoginMode()
    object WhatsAppOtp : LoginMode()
    object SmsOtp : LoginMode()
}