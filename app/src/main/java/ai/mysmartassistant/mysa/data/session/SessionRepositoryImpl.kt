package ai.mysmartassistant.mysa.data.session

import ai.mysmartassistant.mysa.data.auth.dto.ProfileDto
import ai.mysmartassistant.mysa.domain.session.SessionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class SessionRepositoryImpl @Inject constructor(
    private val localDataSource: SessionLocalDataSource
) : SessionRepository {
    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        localDataSource.setLoggedIn(isLoggedIn)
    }

    override suspend fun clearSession() {
        localDataSource.clearSession()
    }

    override suspend fun hasValidSession(): Boolean {
        return localDataSource.isLoggedInFlow.first()
    }

    override val isLoggedInFlow: Flow<Boolean>
        get() = localDataSource.isLoggedInFlow

    override suspend fun getAuthToken(): String? = localDataSource.authTokenFlow.firstOrNull()

    override suspend fun getUserId(): String? = localDataSource.userIdFlow.firstOrNull()

    override suspend fun saveProfile(profile: ProfileDto) {
        localDataSource.saveProfile(profile = profile)
    }

    override val profileFlow = localDataSource.profileFlow
}