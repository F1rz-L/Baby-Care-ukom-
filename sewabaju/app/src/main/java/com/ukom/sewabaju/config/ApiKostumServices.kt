package com.ukom.sewabaju.config

import com.ukom.sewabaju.model.Kostum
import com.ukom.sewabaju.model.request.KostumRequest
import com.ukom.sewabaju.model.response.APIResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiKostumServices {
    @GET("kostums/category/{category_id}")
    fun getKostumByCategory(
        @Path("category_id") categoryId: Int,
        @Query("search") search: String,
    ): Call<APIResponse<List<Kostum>>>

    @POST("kostums")
    fun insertKostum(
        @Body kostumRequest: KostumRequest
    ): Call<APIResponse<Kostum>>

    @GET("kostums/{id}")
    fun getKostumDetail(
        @Path("id") id: Int,
    ): Call<APIResponse<Kostum>>

    @PUT("kostums/{id}")
    fun updateKostum(
        @Path("id") id: Int,
        @Body kostumRequest: KostumRequest
    ): Call<APIResponse<Kostum>>

    @DELETE("kostums/{id}")
    fun deleteKostum(
        @Path("id") id: Int,
    ): Call<APIResponse<Kostum>>

    @GET
    fun getKostumImage(
        @Url imageUrl: String
    ): Call<ResponseBody>

    @Multipart
    @POST("kostums/upload/{id}")
    fun uploadKostumImage(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): Call<APIResponse<Kostum>>
}