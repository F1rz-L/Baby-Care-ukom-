package com.konyol.babycarex.data.request

data class PasswordRequest(
    var email: String = "",
    var otp: String = "",
    var password: String = "",
    var password_confirmation: String = ""
)