package com.ukom.sewabaju.model.response

import com.google.gson.annotations.SerializedName

data class Laporan(
    @SerializedName("order_masuk")
    val orderMasuk: String,
    @SerializedName("order_proses")
    val orderProses: String,
    @SerializedName("order_masa_pinjam")
    val orderMasaPinjam: String,
    @SerializedName("order_selesai")
    val orderSelesai: String,
    @SerializedName("order_batal")
    val orderBatal: String,
    @SerializedName("total_pemasukan")
    val totalPemasukan: String,
)