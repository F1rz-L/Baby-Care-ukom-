package com.konyol.babycarex.data.model

data class Barang(
    val id: Int? = null,
    val id_kategori: Int? = null,
    val id_peminjam: Int? = null,
    val namabarang: String,
    val merk: String,
    val deskripsi: String,
    val harga: Int,
    val gambar: String? = null,
    val status: String
)
