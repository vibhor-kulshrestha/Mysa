package ai.mysmartassistant.mysa.core.network

import retrofit2.HttpException
import java.io.IOException

sealed class ApiError {
    // Common
    object Network : ApiError()
    object Unauthorized : ApiError()
    object Forbidden : ApiError()
    object Timeout : ApiError()

    // Validation / client errors
    data class BadRequest(val message: String?) : ApiError()
    data class Validation(val errors: Map<String, String>?) : ApiError()

    // Server
    object Server : ApiError()

    // Fallback
    data class Unknown(val message: String?) : ApiError()
}

fun mapHttpError(e: HttpException): ApiError {
    return when (e.code()) {
        400 -> ApiError.BadRequest(e.message())
        401 -> ApiError.Unauthorized
        403 -> ApiError.Forbidden
        408 -> ApiError.Timeout
        in 500..599 -> ApiError.Server
        else -> ApiError.Unknown(e.message())
    }
}

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (e: HttpException) {
        ApiResult.Error(mapHttpError(e))
    } catch (e: IOException) {
        ApiResult.Error(ApiError.Network)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }
}