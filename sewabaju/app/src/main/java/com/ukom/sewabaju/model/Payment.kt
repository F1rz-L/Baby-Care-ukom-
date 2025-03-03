package com.ukom.sewabaju.model

import com.google.gson.annotations.SerializedName

data class Payment(
    val id: Int,
    @SerializedName("order_id")
    val orderId: Int,
    val amount: String,
    val status: String,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("created_at")
    val createdAt: String,
    val order: Order? = null
)