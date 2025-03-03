package com.ukom.sewabaju.model.response

import com.ukom.sewabaju.model.User

data class AuthResponse(
    val code: Int,
    val status: Boolean,
    val message: String,
    val data: AuthData
) {
    data class AuthData(
        val token: String? = null,
        val user: User? = null
    )
}