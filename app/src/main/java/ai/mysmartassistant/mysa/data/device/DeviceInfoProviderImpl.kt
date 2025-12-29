package ai.mysmartassistant.mysa.data.device

import ai.mysmartassistant.mysa.domain.device.DeviceIdProvider
import ai.mysmartassistant.mysa.domain.device.DeviceInfoProvider
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.util.Locale
import java.util.TimeZone

class DeviceInfoProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val deviceIdProvider: DeviceIdProvider
) : DeviceInfoProvider {

    override fun appVersion(): String {
        return try {
            val pInfo = context.packageManager
                .getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    }

    override suspend fun deviceId(): String {
        return deviceIdProvider.getDeviceId()
    }


    override fun timeZone(): String {
        val offsetMillis = TimeZone.getDefault().getOffset(System.currentTimeMillis())
        return formatTimeZone(offsetMillis)
    }

    fun formatTimeZone(offsetMillis: Int): String {
        val sign = if (offsetMillis < 0) "-" else "+"
        val absMillis = kotlin.math.abs(offsetMillis)

        val hours = absMillis / (1000 * 60 * 60)
        val minutes = (absMillis / (1000 * 60)) % 60

        return String.format(
            Locale.US,
            "UTC%s%02d:%02d",
            sign,
            hours,
            minutes
        )
    }
}