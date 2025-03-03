package com.ukom.sewabaju.model.request

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("rental_start_date")
    val rentalStartDate: String,
    @SerializedName("rental_end_date")
    val rentalEndDate: String,
    @SerializedName("total_price")
    val totalPrice: String,
    @SerializedName("order_status")
    val orderStatus: String,
    @SerializedName("payment_status")
    val paymentStatus: String,
    @SerializedName("return_date")
    val returnDate: String? = null
)