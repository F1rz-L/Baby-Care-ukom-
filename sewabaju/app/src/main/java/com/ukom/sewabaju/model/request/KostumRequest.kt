package com.ukom.sewabaju.model.request

import com.google.gson.annotations.SerializedName
import com.ukom.sewabaju.model.SizeStockPrice

data class KostumRequest(
    val name: String,
    val description: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("size_stock_price_data")
    val sizeStockPriceData: List<SizeStockPrice>,
)