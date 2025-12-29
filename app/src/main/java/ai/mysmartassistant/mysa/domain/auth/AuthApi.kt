package ai.mysmartassistant.mysa.domain.auth

import ai.mysmartassistant.mysa.data.auth.dto.LoginResponseDto
import ai.mysmartassistant.mysa.data.auth.dto.SendOtpRequestDto
import ai.mysmartassistant.mysa.data.auth.dto.VerifyRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @GET("api/login/mediums")
    suspend fun getLoginOptions(
        @Query("countryCode") countryCode: String
    ): List<String>

    @POST("api/login/app/verify")
    suspend fun verifyLogin(
        @Body body: VerifyRequestDto
    ): LoginResponseDto

    @POST("api/login/app/{medium}/otp")
    suspend fun sendOtp(
        @Path("medium") medium: String,
        @Body body: SendOtpRequestDto
    )
}