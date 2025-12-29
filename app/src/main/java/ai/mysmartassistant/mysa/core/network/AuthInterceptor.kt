package ai.mysmartassistant.mysa.core.network

import ai.mysmartassistant.mysa.domain.session.SessionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val sessionRepository: SessionRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = runBlocking {
            sessionRepository.getAuthToken()
        }

        val newRequest = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .addHeader("access-token", token)
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}