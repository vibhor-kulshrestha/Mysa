package ai.mysmartassistant.mysa.data.location

import ai.mysmartassistant.mysa.domain.location.CountryDialingCodeResolver
import jakarta.inject.Inject

class CountryDialingCodeResolverImpl @Inject constructor() :
    CountryDialingCodeResolver {

    private val map = mapOf(
        "IN" to "+91",
        "US" to "+1",
        "GB" to "+44"
        // extend as needed
    )

    override fun getDialingCode(countryIso: String): String {
        return map[countryIso.uppercase()] ?: "+1" // fallback
    }
}