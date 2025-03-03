package com.ukom.sewabaju.model.request

import com.google.gson.annotations.SerializedName

class PaymentRequest(
    @SerializedName("order_id")
    val orderId: Int,
    val amount: String,
    val status: String,
    @SerializedName("payment_method")
    val paymentMethod: String,
)