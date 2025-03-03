package com.ukom.sewabaju.config

import com.ukom.sewabaju.model.User
import com.ukom.sewabaju.model.request.UserRequest
import com.ukom.sewabaju.model.response.APIResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Url

interface ApiUserServices {
    @PUT("user/edit")
    fun updateUser(
        @Body userRequest: UserRequest
    ): Call<APIResponse<User>>

    @GET
    fun getUserImage(
        @Url imageUrl: String
    ): Call<ResponseBody>

    @Multipart
    @POST("user/upload")
    fun uploadUserImage(
        @Part image: MultipartBody.Part
    ): Call<APIResponse<User>>
}