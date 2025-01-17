package com.konyol.babycarex.data.request

data class RegisterRequest(
    val nama: String,
    val email: String,
    val password: String,
    val alamat: String,
    val nomor_telepon: String,
    val nik: String,

)
