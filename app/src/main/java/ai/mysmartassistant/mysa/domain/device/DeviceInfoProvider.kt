package ai.mysmartassistant.mysa.domain.device

interface DeviceInfoProvider {
    fun appVersion(): String
    suspend fun deviceId(): String
    fun timeZone(): String
}