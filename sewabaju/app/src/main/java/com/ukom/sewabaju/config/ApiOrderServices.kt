package com.ukom.sewabaju.config

import com.ukom.sewabaju.model.Order
import com.ukom.sewabaju.model.request.OrderRequest
import com.ukom.sewabaju.model.response.APIResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiOrderServices {
    @GET("orders")
    fun getOrders(
        @Query("orderStatus") orderStatus: String,
        @Query("namaPelanggan") namaPelanggan: String
    ): Call<APIResponse<List<Order>>>

    @POST("orders")
    fun insertOrder(
        @Body orderRequest: OrderRequest
    ): Call<APIResponse<Order>>

    @GET("orders/{id}")
    fun getOrder(
        @Path("id") orderId: Int,
    ): Call<APIResponse<Order>>

    @PUT("orders/{id}")
    fun updateOrder(
        @Path("id") orderId: Int,
        @Body orderRequest: OrderRequest
    ): Call<APIResponse<Order>>

    @DELETE("orders/{id}")
    fun deleteOrder(
        @Path("id") orderId: Int,
    ): Call<APIResponse<Order>>
}