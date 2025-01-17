package com.konyol.babycarex.data.network

import com.konyol.babycarex.data.model.User
import com.konyol.babycarex.data.request.PasswordRequest
import com.konyol.babycarex.data.request.RegisterRequest
import com.konyol.babycarex.data.response.APIResponse
import com.konyol.babycarex.data.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {
    @GET("auth/me")
    suspend fun getCurrentUser() : Response<APIResponse<User>>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest) : Response<LoginResponse>

    @FormUrlEncoded
    @POST("auth/verify")
    suspend fun requestOTP(@Field("email") email: String) : Response<APIResponse<User>>

    @FormUrlEncoded
    @PUT("auth/verify")
    suspend fun verifyOTP(
        @Field("email") email: String,
        @Field("otp") otp: String
    ) : Response<APIResponse<User>>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body passwordRequest: PasswordRequest) : Response<APIResponse<User>>

    @PUT("auth/forgot-password")
    suspend fun updatePassword(@Body passwordRequest: PasswordRequest) : Response<APIResponse<User>>

    @POST("auth/logout")
    suspend fun logout() : Response<APIResponse<User>>
}