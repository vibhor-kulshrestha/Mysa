package ai.mysmartassistant.mysa.platform.sms

fun extractOtp(message: String): String? {
    // Extract first 4–6 digit number
    val regex = Regex("\\b\\d{4,6}\\b")
    return regex.find(message)?.value
}