package ai.mysmartassistant.mysa.data.session

import ai.mysmartassistant.mysa.data.auth.dto.ProfileDto
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_ID = stringPreferencesKey("user_id")

        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val GENDER = stringPreferencesKey("gender")
        val LANGUAGE = stringPreferencesKey("language")
    }

    val isLoggedInFlow: Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[Keys.IS_LOGGED_IN] ?: false }

    suspend fun setLoggedIn(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = value
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { it[Keys.AUTH_TOKEN] = token }
    }

    val authTokenFlow: Flow<String?> =
        dataStore.data.map { it[Keys.AUTH_TOKEN] }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { it[Keys.USER_ID] = userId }
    }

    val userIdFlow: Flow<String?> =
        dataStore.data.map { it[Keys.USER_ID] }

    suspend fun saveProfile(profile: ProfileDto) {
        dataStore.edit { prefs ->
            profile.firstName?.let {

                prefs[Keys.FIRST_NAME] = it
            } ?: prefs.remove(Keys.FIRST_NAME)

            profile.lastName?.let {
                prefs[Keys.LAST_NAME] = it
            } ?: prefs.remove(Keys.LAST_NAME)

            profile.gender?.let {
                prefs[Keys.GENDER] = it
            } ?: prefs.remove(Keys.GENDER)

            profile.language?.let {
                prefs[Keys.LANGUAGE] = it
            } ?: prefs.remove(Keys.LANGUAGE)
        }
    }

    val profileFlow: Flow<ProfileDto?> =
        dataStore.data.map { prefs ->
            val firstName = prefs[Keys.FIRST_NAME] ?: return@map null
            ProfileDto(
                firstName = firstName,
                lastName = prefs[Keys.LAST_NAME],
                gender = prefs[Keys.GENDER],
                language = prefs[Keys.LANGUAGE]
            )
        }
}