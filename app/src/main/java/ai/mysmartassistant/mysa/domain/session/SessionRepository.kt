package ai.mysmartassistant.mysa.domain.session

import ai.mysmartassistant.mysa.data.auth.dto.ProfileDto
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun setLoggedIn(isLoggedIn: Boolean)

    suspend fun clearSession()

    suspend fun hasValidSession(): Boolean

    val isLoggedInFlow: Flow<Boolean>

    suspend fun getAuthToken(): String?

    suspend fun getUserId(): String?

    suspend fun saveProfile(profile: ProfileDto)

    val profileFlow: Flow<ProfileDto?>
}