package ai.mysmartassistant.mysa.data.auth.dto

data class PhoneLoginDto(
    val msisdn: String,
    val smsOtp: String
)
