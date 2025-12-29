package ai.mysmartassistant.mysa.data.auth.dto

data class LanguagePreferenceDto(
    val id: Int,
    val languageCode: String,
    val languageUrl: String,
    val defaultLanguage: Boolean,
)