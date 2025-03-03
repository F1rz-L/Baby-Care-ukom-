package com.ukom.sewabaju.config

import com.ukom.sewabaju.model.response.APIResponse
import com.ukom.sewabaju.model.response.Laporan
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiLaporanServices {
    @GET("laporan")
    fun getLaporan(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): Call<APIResponse<Laporan>>
}