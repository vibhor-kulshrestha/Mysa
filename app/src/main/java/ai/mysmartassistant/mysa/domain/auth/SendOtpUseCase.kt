package ai.mysmartassistant.mysa.domain.auth

import ai.mysmartassistant.mysa.core.network.ApiResult
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