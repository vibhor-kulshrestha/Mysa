package ai.mysmartassistant.mysa.core.network

import ai.mysmartassistant.mysa.domain.session.SessionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class UserContextInterceptor @Inject constructor(
    private val sessionRepository: SessionRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val userId = runBlocking {
            sessionRepository.getUserId()
        }

        // If no userId, proceed as-is (login APIs, etc.)
        if (userId.isNullOrBlank()) {
            return chain.proceed(original)
        }

        val newUrl = original.url.newBuilder()
            .addQueryParameter("userId", userId)
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}