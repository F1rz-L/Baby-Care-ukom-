package com.ukom.sewabaju.config

import com.ukom.sewabaju.model.Order
import com.ukom.sewabaju.model.OrderItem
import com.ukom.sewabaju.model.request.OrderItemRequest
import com.ukom.sewabaju.model.response.APIResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiOrderItemServices {
    @POST("order-items")
    fun insertOrderItem(
        @Body orderItemRequest: OrderItemRequest
    ): Call<APIResponse<OrderItem>>
}