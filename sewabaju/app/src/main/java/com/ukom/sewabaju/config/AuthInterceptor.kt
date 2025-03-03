package com.ukom.sewabaju.config

import android.content.Context
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.getCustomData
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        context.getCustomData("token")?.let {
            builder.addHeader("Authorization", "Bearer $it")
        }

        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}