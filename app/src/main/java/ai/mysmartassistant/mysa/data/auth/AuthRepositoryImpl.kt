package ai.mysmartassistant.mysa.data.auth

import ai.mysmartassistant.mysa.data.auth.dto.LoginResponseDto
import ai.mysmartassistant.mysa.data.auth.dto.SendOtpRequestDto
import ai.mysmartassistant.mysa.data.auth.dto.VerifyRequestDto
import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.domain.auth.AuthApi
import ai.mysmartassistant.mysa.domain.auth.AuthRepository
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.core.network.safeApiCall
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun getAvailableLoginMediums(countryCode: String): ApiResult<List<LoginMedium>> {
        return safeApiCall {
            api.getLoginOptions(countryCode)
                .mapNotNull { medium ->
                    when (medium) {
                        "WHATSAPP" -> LoginMedium.WHATSAPP
                        "TRUECALLER" -> LoginMedium.TRUECALLER
                        "SMS" -> LoginMedium.SMS
                        else -> null
                    }
                }
        }
    }


    override suspend fun verifyLogin(verifyRequestDto: VerifyRequestDto): ApiResult<LoginResponseDto> =
        safeApiCall {
            api.verifyLogin(verifyRequestDto)
        }


    override suspend fun sendOtp(
        medium: LoginMedium,
        mobileNumber: String
    ): ApiResult<Unit> = safeApiCall {
        api.sendOtp(
            medium = medium.name.uppercase(),
            body = SendOtpRequestDto(mobileNumber)
        )
    }
}