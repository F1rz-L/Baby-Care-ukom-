package com.ukom.sewabaju.repository

import android.view.View
import com.ukom.sewabaju.config.NetworkConfig
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.helper.RetrofitHelper
import com.ukom.sewabaju.model.User
import com.ukom.sewabaju.model.request.LoginRequest
import com.ukom.sewabaju.model.request.RegisterRequest
import com.ukom.sewabaju.model.response.AuthResponse

class AuthRepository(
    val view: View
) {
    private val retrofit = NetworkConfig(view.context).getAuthServices()

    fun login(
        loginRequest: LoginRequest,
        onSuccess: (AuthResponse.AuthData) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.login(loginRequest), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                onFailure()
            }
            if (it!!.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)

            onFailure()
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
            onFailure()
        })
    }

    fun register(
        registerRequest: RegisterRequest,
        onSuccess: (AuthResponse.AuthData) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.register(registerRequest), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                onFailure()
            }
            if (it!!.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)

            onFailure()
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
            onFailure()
        })
    }

    fun me(
        onSuccess: (User) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.me(), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }

    fun logout(
        onSuccess: (User) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.logout(), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                onFailure()
            }
            if (it!!.status) onSuccess(it.data) else onFailure()
        }, {
            onFailure()
        })
    }

    fun forgotPassword(
        email: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.forgotPassword(email), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                onFailure()
            }
            if (it!!.status) onSuccess() else MessageHelper.errorResult(view, it.message)

            onFailure()
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
            onFailure()
        })
    }

    fun updatePassword(
        email: String,
        otp: String,
        password: String,
        passwordConfirmation: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.updatePassword(email, otp, password, passwordConfirmation), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                onFailure()
            }
            if (it!!.status) onSuccess() else MessageHelper.errorResult(view, it.message)

            onFailure()
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
            onFailure()
        })
    }
}