package ai.mysmartassistant.mysa.data.auth.dto

data class TruecallerLoginDto(
    val clientId: String,
    val codeVerifier: String,
    val code: String
)
