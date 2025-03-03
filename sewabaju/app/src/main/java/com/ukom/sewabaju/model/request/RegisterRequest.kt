package com.ukom.sewabaju.model.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val address: String,
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String
)