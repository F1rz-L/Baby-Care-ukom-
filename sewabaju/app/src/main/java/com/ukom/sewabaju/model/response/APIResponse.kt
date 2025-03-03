package com.ukom.sewabaju.model.response

data class APIResponse<T> (
    val code: Int,
    val status: Boolean,
    val message: String,
    val data: T
)