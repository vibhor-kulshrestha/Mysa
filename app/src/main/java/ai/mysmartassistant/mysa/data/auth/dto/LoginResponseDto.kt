package ai.mysmartassistant.mysa.data.auth.dto

data class LoginResponseDto(
    val authToken: String,
    val userId: Long,
    val newUser: Boolean,
    val languagePreferenceDto: List<LanguagePreferenceDto>?,
    val profileDto: ProfileDto?
)
