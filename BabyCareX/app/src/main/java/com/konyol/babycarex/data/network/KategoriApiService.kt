package com.konyol.babycarex.data.network

import com.konyol.babycarex.data.model.Kategori
import com.konyol.babycarex.data.response.APIResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KategoriApiService {
    @GET("kategori")
    suspend fun getAllKategori(): Response<APIResponse<List<Kategori>>>

    @POST("kategori")
    suspend fun addKategori(@Body kategori: Kategori): Response<APIResponse<Kategori>>

    @GET("kategori/{id}")
    suspend fun getKategoriById(@Path("id") id: Int): Response<APIResponse<Kategori>>

    @PUT("kategori/{id}")
    suspend fun updateKategori(@Path("id") id: Int, @Body kategori: Kategori): Response<APIResponse<Kategori>>

    @DELETE("kategori/{id}")
    suspend fun deleteKategori(@Path("id") id: Int): Response<APIResponse<Kategori>>
}