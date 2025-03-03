package com.ukom.sewabaju.model.request

import com.google.gson.annotations.SerializedName

data class OrderItemRequest(
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("kostum_id")
    val kostumId: Int,
    val size: String,
    val price: String,
    val quantity: Int,
)