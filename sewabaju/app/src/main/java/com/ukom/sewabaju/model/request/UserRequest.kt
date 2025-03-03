package com.ukom.sewabaju.model.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    val name: String,
    val email: String,
    val address: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
)