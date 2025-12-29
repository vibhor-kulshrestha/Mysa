package ai.mysmartassistant.mysa.ui.auth

data class PhoneInputState(
    val countryCode: String,
    val countryIso: String,
    val phoneNumber: String = ""
) {
    val isValid: Boolean
        get() = phoneNumber.length >= 8
}