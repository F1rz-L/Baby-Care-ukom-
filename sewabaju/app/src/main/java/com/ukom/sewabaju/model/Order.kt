package com.ukom.sewabaju.model

import com.google.gson.annotations.SerializedName

data class Order(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    val faktur: String,
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
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("return_date")
    val returnDate: String? = null,
    val user: User? = null,
    @SerializedName("order_items")
    val orderItem: List<OrderItem>? = null,
    @SerializedName("payments")
    val payment: List<Payment>? = null,
)