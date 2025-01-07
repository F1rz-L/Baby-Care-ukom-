package com.konyol.babycarex.data.network

import com.konyol.babycarex.data.model.Pelanggan
import com.konyol.babycarex.data.response.APIResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PelangganApiService {
    @GET("pelanggan")
    suspend fun getAllPelanggan(): Response<APIResponse<List<Pelanggan>>>

    @POST("pelanggan")
    suspend fun addPelanggan(@Body pelanggan: Pelanggan): Response<APIResponse<Pelanggan>>

    @GET("pelanggan/{id}")
    suspend fun getPelangganById(@Path("id") id: Int): Response<APIResponse<Pelanggan>>

    @PUT("pelanggan/{id}")
    suspend fun updatePelanggan(@Path("id") id: Int, @Body pelanggan: Pelanggan): Response<APIResponse<Pelanggan>>

    @DELETE("pelanggan/{id}")
    suspend fun deletePelanggan(@Path("id") id: Int): Response<APIResponse<Pelanggan>>
}