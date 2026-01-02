package ai.mysmartassistant.mysa.domain.usecase.auth

import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.core.network.mapSuccess
import ai.mysmartassistant.mysa.domain.auth.AuthRepository
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.domain.auth.TruecallerCapability
import ai.mysmartassistant.mysa.domain.location.CountryProvider
import jakarta.inject.Inject

class FetchLoginOptionsUseCase @Inject constructor(
    private val countryProvider: CountryProvider,
    private val authRepository: AuthRepository,
    private val truecallerCapability: TruecallerCapability
) {
    suspend operator fun invoke() : ApiResult<List<LoginMedium>> {
        val countryCode = countryProvider.getCountryCode()
        return authRepository
            .getAvailableLoginMediums(countryCode)
            .mapSuccess { serverMediums ->
                serverMediums.filterNot {
                    it == LoginMedium.TRUECALLER &&
                            !truecallerCapability.isUsable()
                }
            }
    }
}