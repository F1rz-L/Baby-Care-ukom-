package com.konyol.babycarex.activity.kasir

import com.google.gson.annotations.SerializedName

data class ListBarangModel(
//    @field:SerializedName("id")
//    val id: Int? = null,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("merk")
    val merk: String,

    @field:SerializedName("kategori")
    val kategori: String,

    @field:SerializedName("gambar")
    val gambar: Int,

    @field:SerializedName("harga")
    val harga: String
)

