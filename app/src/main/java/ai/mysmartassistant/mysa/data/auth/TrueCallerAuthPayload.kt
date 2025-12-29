package ai.mysmartassistant.mysa.data.auth

data class TrueCallerAuthPayload(
    val authorizationCode: String,
    val codeVerifier: String,
    val state: String,
    val clientId: String
)
