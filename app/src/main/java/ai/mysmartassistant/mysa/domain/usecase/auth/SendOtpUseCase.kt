package ai.mysmartassistant.mysa.domain.usecase.auth

import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.domain.auth.AuthRepository
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        medium: LoginMedium,
        msisdn: String
    ) : ApiResult<Unit> {
        return authRepository.sendOtp(
            medium = medium,
            mobileNumber = msisdn
        )
    }
}