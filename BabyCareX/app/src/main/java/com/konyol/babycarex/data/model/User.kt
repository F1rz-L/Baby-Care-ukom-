package com.konyol.babycarex.data.model

data class User(
    val id: Int? = null,
    val nama: String,
    val email: String,
    val alamat: String,
    val nomor_telepon: String,
    val nik: String,
    val otp: Int,
    val email_verified_at: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)
