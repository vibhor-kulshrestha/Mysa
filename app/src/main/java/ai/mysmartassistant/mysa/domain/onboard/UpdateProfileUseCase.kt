package ai.mysmartassistant.mysa.domain.onboard

import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.core.network.mapSuccess
import ai.mysmartassistant.mysa.data.onboard.dto.ProfileRequestDto
import ai.mysmartassistant.mysa.data.onboard.dto.toProfileDto
import ai.mysmartassistant.mysa.domain.session.SessionRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val onboardingApi: OnboardRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        gender: String,
        language: String
    ): ApiResult<Unit> {
        val profileResponseDto = ProfileRequestDto(
            firstName = firstName,
            lastName = lastName,
            gender = gender,
            language = language
        )
        return onboardingApi.updateProfile(profileResponseDto).mapSuccess {
            sessionRepository.saveProfile(profileResponseDto.toProfileDto())
        }
    }
}