package ai.mysmartassistant.mysa.data.device

import ai.mysmartassistant.mysa.domain.device.DeviceIdProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import java.util.UUID

private val DEVICE_ID_KEY = stringPreferencesKey("device_uuid")
class DeviceIdProviderImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DeviceIdProvider {

    override suspend fun getDeviceId(): String {
        val prefs = dataStore.data.first()

        val existing = prefs[DEVICE_ID_KEY]
        if (existing != null) return existing

        val newId = UUID.randomUUID().toString()

        dataStore.edit { it[DEVICE_ID_KEY] = newId }

        return newId
    }
}