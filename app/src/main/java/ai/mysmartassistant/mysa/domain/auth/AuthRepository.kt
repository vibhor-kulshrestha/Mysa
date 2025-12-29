package ai.mysmartassistant.mysa.domain.auth

import ai.mysmartassistant.mysa.data.auth.dto.LoginResponseDto
import ai.mysmartassistant.mysa.data.auth.dto.VerifyRequestDto
import ai.mysmartassistant.mysa.core.network.ApiResult

interface AuthRepository {
    suspend fun getAvailableLoginMediums(
        countryCode: String
    ): ApiResult<List<LoginMedium>>

    suspend fun verifyLogin(
        verifyRequestDto: VerifyRequestDto
    ): ApiResult<LoginResponseDto>

    suspend fun sendOtp(
        medium: LoginMedium,
        mobileNumber: String
    ) : ApiResult<Unit>
}