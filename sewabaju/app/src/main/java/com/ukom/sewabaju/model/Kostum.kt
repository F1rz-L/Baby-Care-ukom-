package com.ukom.sewabaju.model

import com.google.gson.annotations.SerializedName

data class Kostum(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("size_stock_price_data")
    val sizeStockPrice: List<SizeStockPrice>,
    val image: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val category: Category? = null
)