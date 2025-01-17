package com.konyol.babycarex.config

import android.content.Context
import android.content.Intent
import android.util.Log
import com.konyol.babycarex.activity.guest.LoginActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val localDataManager: LocalDataManager,
    @ApplicationContext private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = localDataManager.getToken() // Get token from LocalDataManager

        // Build a new request with the Authorization header if the token exists
        val requestBuilder: Request.Builder = originalRequest.newBuilder()
        val urlBuilder: HttpUrl.Builder = originalRequest.url.newBuilder()

        if (!token.isNullOrEmpty()) {
//            Log.d("AuthInterceptor", "Token: $token")
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())
        val responseCode = response.code

        if (responseCode == 401) {
            localDataManager.clearToken()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        return response
    }

}