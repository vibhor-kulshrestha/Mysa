package ai.mysmartassistant.mysa.domain.device

interface DeviceIdProvider {
    suspend fun getDeviceId(): String
}