package ai.mysmartassistant.mysa.data.location

import ai.mysmartassistant.mysa.domain.location.CountryProvider
import android.content.Context
import android.telephony.TelephonyManager
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.util.Locale

class CountryProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : CountryProvider {
    override fun getCountryCode(): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val simCountry = tm.simCountryIso
        if (!simCountry.isNullOrBlank() && simCountry.length == 2) {
            return simCountry.uppercase()
        }

        if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
            val networkCountry = tm.networkCountryIso
            if (!networkCountry.isNullOrBlank() && networkCountry.length == 2) {
                return networkCountry.uppercase()
            }
        }

        // fallback
        return Locale.getDefault().country.uppercase()
    }
}