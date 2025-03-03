package com.ukom.sewabaju.config

import com.ukom.sewabaju.model.Category
import com.ukom.sewabaju.model.request.CategoryRequest
import com.ukom.sewabaju.model.response.APIResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiCategoryServices {
    @GET("category")
    fun getCategories(): Call<APIResponse<List<Category>>>

    @POST("category")
    fun insertCategory(
        @Body categoryRequest: CategoryRequest
    ): Call<APIResponse<Category>>

    @GET("category/{id}")
    fun getCategoryDetail(
        @Path("id") id: Int,
    ): Call<APIResponse<Category>>

    @PUT("category/{id}")
    fun updateCategory(
        @Path("id") id: Int,
        @Body categoryRequest: CategoryRequest
    ): Call<APIResponse<Category>>

    @DELETE("category/{id}")
    fun deleteCategory(
        @Path("id") id: Int
    ): Call<APIResponse<Category>>
}