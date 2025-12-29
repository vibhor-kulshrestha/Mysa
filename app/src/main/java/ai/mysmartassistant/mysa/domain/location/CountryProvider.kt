package ai.mysmartassistant.mysa.domain.location

interface CountryProvider {
    fun getCountryCode(): String
}