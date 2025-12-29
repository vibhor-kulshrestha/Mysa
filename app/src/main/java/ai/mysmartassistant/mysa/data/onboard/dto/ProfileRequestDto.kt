package ai.mysmartassistant.mysa.data.onboard.dto

import ai.mysmartassistant.mysa.data.auth.dto.ProfileDto

data class ProfileRequestDto(
    val firstName : String,
    val lastName : String,
    val gender : String,
    val language : String
)

fun ProfileRequestDto.toProfileDto() : ProfileDto {
    return ProfileDto(
        firstName = firstName,
        lastName = lastName,
        gender = gender,
        language = language,
    )
}