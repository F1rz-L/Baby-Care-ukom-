package com.konyol.babycarex.data.model

data class Pelanggan(
    val id: Int? = null,
    val id_peminjaman: Int? = null,
    val nama: String,
    val alamat: String,
    val nomor_telepon: String,
    val nik: String,
    val created_at: String? = null,
    val updated_at: String? = null
)
