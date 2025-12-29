package ai.mysmartassistant.mysa.domain.onboard

import ai.mysmartassistant.mysa.data.onboard.dto.ProfileRequestDto
import ai.mysmartassistant.mysa.core.network.ApiResult

interface OnboardRepository {
    suspend fun updateProfile(
        profileResponseDto: ProfileRequestDto
    ) : ApiResult<Unit>
}