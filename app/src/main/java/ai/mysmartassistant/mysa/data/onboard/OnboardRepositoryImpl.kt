package ai.mysmartassistant.mysa.data.onboard

import ai.mysmartassistant.mysa.data.onboard.dto.ProfileRequestDto
import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.domain.onboard.OnboardApi
import ai.mysmartassistant.mysa.domain.onboard.OnboardRepository
import ai.mysmartassistant.mysa.core.network.safeApiCall
import javax.inject.Inject

class OnboardRepositoryImpl @Inject constructor(
    private val api: OnboardApi,
) : OnboardRepository {

    override suspend fun updateProfile(
        profileResponseDto: ProfileRequestDto
    ): ApiResult<Unit> {
        return safeApiCall {
            api.updateProfile(profileResponseDto)
        }
    }
}