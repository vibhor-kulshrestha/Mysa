package ai.mysmartassistant.mysa.domain.location

interface CountryDialingCodeResolver {
    fun getDialingCode(countryIso: String): String
}