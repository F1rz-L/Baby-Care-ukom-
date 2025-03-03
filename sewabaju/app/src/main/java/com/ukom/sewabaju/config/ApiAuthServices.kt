package com.ukom.sewabaju.config

import com.ukom.sewabaju.model.User
import com.ukom.sewabaju.model.request.LoginRequest
import com.ukom.sewabaju.model.request.RegisterRequest
import com.ukom.sewabaju.model.response.APIResponse
import com.ukom.sewabaju.model.response.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiAuthServices {
    @POST("register")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<AuthResponse>

    @POST("login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<AuthResponse>

    @GET("me")
    fun me(): Call<APIResponse<User>>

    @POST("logout")
    fun logout(): Call<APIResponse<User>>

    @FormUrlEncoded
    @POST("forgot-password")
    fun forgotPassword(
        @Field("email") email: String,
    ): Call<APIResponse<MutableMap<String, Any>>>

    @FormUrlEncoded
    @PUT("forgot-password")
    fun updatePassword(
        @Field("email") email: String,
        @Field("otp") otp: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
    ): Call<APIResponse<MutableMap<String, Any>>>
}