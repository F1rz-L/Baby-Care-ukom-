package com.konyol.babycarex.data.model

data class Pinjaman(
    val id: Int? = null,
    val id_peminjam: Int,
    val id_barang: Int,
    val tgl_pinjam: String,
    val tgl_kembali: String,
    val status: String,
    val denda: Int,
    val created_at: String? = null,
    val updated_at: String? = null
)
