package com.konyol.babycarex.data.response

import com.konyol.babycarex.data.model.Kategori

data class APIResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)