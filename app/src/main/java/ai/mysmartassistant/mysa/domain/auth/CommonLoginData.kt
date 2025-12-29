package ai.mysmartassistant.mysa.domain.auth

data class CommonLoginData(
    val loginPlatform: String,
    val appVersion: String,
    val deviceId: String,
    val countryCode: String,
    val timeZone: String
)