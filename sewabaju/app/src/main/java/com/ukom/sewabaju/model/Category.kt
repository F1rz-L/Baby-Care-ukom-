package com.ukom.sewabaju.model

import com.google.gson.annotations.SerializedName

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
)