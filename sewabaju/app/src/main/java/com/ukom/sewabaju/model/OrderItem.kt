package com.ukom.sewabaju.model

import com.google.gson.annotations.SerializedName

data class OrderItem(
    val id: Int,
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("kostum_id")
    val kostumId: Int,
    val size: String,
    val price: String,
    val quantity: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val order: Order? = null,
    val kostum: Kostum? = null,
)