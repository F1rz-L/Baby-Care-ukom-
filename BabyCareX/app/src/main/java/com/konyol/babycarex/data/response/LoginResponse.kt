package com.konyol.babycarex.data.response

import com.konyol.babycarex.data.model.Kategori
import com.konyol.babycarex.data.model.User

data class LoginResponse(
    val code: Int,
    val success: Boolean,
    val message: String,
    val data: Data
){
    data class Data(
        val user: User,
        val token: String
    )
}
