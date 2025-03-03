package com.konyol.babycarex.data.network

import com.konyol.babycarex.data.model.Barang
import com.konyol.babycarex.data.response.APIResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface BarangApiService {
    @GET("barang")
    suspend fun getAllBarang(): Response<APIResponse<List<Barang>>>

    @POST("barang")
    suspend fun addBarang(@Body barang: Barang): Response<APIResponse<Barang>>

    @GET("barang/{id}")
    suspend fun getBarangById(@Path("id") id: Int): Response<APIResponse<Barang>>

    @PUT("barang/{id}")
    suspend fun updateBarang(
        @Path("id") id: Int,
        @Body barang: Barang
    ): Response<APIResponse<Barang>>

    @DELETE("barang/{id}")
    suspend fun deleteBarang(@Path("id") id: Int): Response<APIResponse<Barang>>

    @Multipart
    @POST("barang/uploadimg/{id}")
    suspend fun uploadBarangImage(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): Response<APIResponse<Barang>>

    @GET("stats/totalbarang")
    suspend fun statsTotalBarang(): Response<Int>
    @GET("stats/barangdipinjam")
    suspend fun statsBarangDipinjam(): Response<Int>
    @GET("stats/totalpinjaman")
    suspend fun statsTotalPinjaman(): Response<Int>
}