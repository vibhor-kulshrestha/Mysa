package ai.mysmartassistant.mysa.domain.usecase.auth

import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.core.network.mapSuccess
import ai.mysmartassistant.mysa.data.auth.dto.PhoneLoginDto
import ai.mysmartassistant.mysa.data.auth.dto.TruecallerLoginDto
import ai.mysmartassistant.mysa.data.auth.dto.VerifyRequestDto
import ai.mysmartassistant.mysa.data.session.SessionLocalDataSource
import ai.mysmartassistant.mysa.domain.auth.AuthRepository
import ai.mysmartassistant.mysa.domain.auth.CommonLoginData
import ai.mysmartassistant.mysa.domain.auth.LoginIntent
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.domain.auth.LoginNextStep
import ai.mysmartassistant.mysa.domain.device.DeviceInfoProvider
import ai.mysmartassistant.mysa.domain.location.CountryProvider
import jakarta.inject.Inject

class VerifyLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val countryProvider: CountryProvider,
    private val deviceInfoProvider: DeviceInfoProvider,
    private val sessionLocalDataSource: SessionLocalDataSource
) {

    suspend fun execute(intent: LoginIntent): ApiResult<LoginNextStep> {
        val common = CommonLoginData(
            loginPlatform = "ANDROID",
            appVersion = deviceInfoProvider.appVersion(),
            deviceId = deviceInfoProvider.deviceId(),
            countryCode = countryProvider.getCountryCode(),
            timeZone = deviceInfoProvider.timeZone()
        )

        val request = when (intent) {

            is LoginIntent.Phone -> {
                VerifyRequestDto(
                    phoneLoginDto = PhoneLoginDto(
                        msisdn = intent.msisdn,
                        smsOtp = intent.otp
                    ),
                    loginMedium = intent.medium.name.uppercase(),
                    loginPlatform = common.loginPlatform,
                    appVersion = common.appVersion,
                    deviceId = common.deviceId,
                    countryCode = common.countryCode,
                    timeZone = common.timeZone
                )
            }

            is LoginIntent.Truecaller -> {
                VerifyRequestDto(
                    trueCallerLoginDto = TruecallerLoginDto(
                        clientId = intent.payload.clientId,
                        codeVerifier = intent.payload.codeVerifier,
                        code = intent.payload.authorizationCode
                    ),
                    loginMedium = LoginMedium.TRUECALLER.name.uppercase(),
                    loginPlatform = common.loginPlatform,
                    appVersion = common.appVersion,
                    deviceId = common.deviceId,
                    countryCode = common.countryCode,
                    timeZone = common.timeZone
                )
            }
        }

        return authRepository.verifyLogin(request).mapSuccess { response ->
            sessionLocalDataSource.setLoggedIn(true)
            sessionLocalDataSource.saveAuthToken(response.authToken)
            sessionLocalDataSource.saveUserId(response.userId.toString())
            response.profileDto?.let {
                sessionLocalDataSource.saveProfile(it)
            }
            if (
                response.newUser ||
                        response.languagePreferenceDto != null
            ) {
                LoginNextStep.Onboarding
            } else {
            LoginNextStep.Home
        }
        }
    }
}