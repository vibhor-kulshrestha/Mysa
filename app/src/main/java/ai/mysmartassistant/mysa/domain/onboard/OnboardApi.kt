package ai.mysmartassistant.mysa.domain.onboard

import ai.mysmartassistant.mysa.data.onboard.dto.ProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface OnboardApi {
    @POST("user/profile/update")
    suspend fun updateProfile(
        @Body body: ProfileRequestDto
    )
}