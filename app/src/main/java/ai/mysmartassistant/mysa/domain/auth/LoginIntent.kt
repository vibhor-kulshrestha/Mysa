package ai.mysmartassistant.mysa.domain.auth

import ai.mysmartassistant.mysa.data.auth.TrueCallerAuthPayload

sealed class LoginIntent {

    data class Phone(
        val msisdn: String,
        val otp: String,
        val medium: LoginMedium // WHATSAPP or SMS
    ) : LoginIntent()

    data class Truecaller(
        val payload: TrueCallerAuthPayload
    ) : LoginIntent()
}