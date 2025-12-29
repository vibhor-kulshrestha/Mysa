package ai.mysmartassistant.mysa.data.auth.dto

data class VerifyRequestDto(
    val phoneLoginDto: PhoneLoginDto? = null,
    val trueCallerLoginDto: TruecallerLoginDto? = null,
    val loginMedium: String,
    val loginPlatform: String,
    val appVersion: String,
    val deviceId: String,
    val countryCode: String,
    val timeZone: String
)