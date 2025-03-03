package com.konyol.babycarex.data.network

import com.konyol.babycarex.data.model.Pinjaman
import com.konyol.babycarex.data.response.APIResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PinjamanApiService {
    @GET("pinjaman")
    suspend fun getAllPinjaman(): Response<APIResponse<List<Pinjaman>>>

    @FormUrlEncoded
    @POST("pinjam")
    suspend fun addPinjaman(
        @Field("id_pelanggan") id_pelanggan: Int,
        @Field("id_barang") id_barang: Int,
        @Field("durasiminggu") durasiminggu: Int
    ): Response<APIResponse<Pinjaman>>

    @POST("pinjaman/{id}/kembali")
    suspend fun kembali(
        @Path("id") id: Int
    ): Response<APIResponse<Pinjaman>>

    @GET("pinjaman/{id}")
    suspend fun getPinjamanById(
        @Path("id") id: Int
    ): Response<APIResponse<Pinjaman>>
}