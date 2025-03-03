package com.ukom.sewabaju.config

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkConfig(
    private val context: Context
) {
    val Base_URL: String = "http://192.168.1.4:8000/api/"

    private fun setOkHttp(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BASIC
        ).setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(AuthInterceptor(context))
            .callTimeout(20L, TimeUnit.SECONDS)
            .build()
    }

    private fun setRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(setOkHttp())
            .build()
    }

    fun getAuthServices(): ApiAuthServices {
        return setRetrofit().create(ApiAuthServices::class.java)
    }

    fun getCategoryServices(): ApiCategoryServices {
        return setRetrofit().create(ApiCategoryServices::class.java)
    }

    fun getKostumServices(): ApiKostumServices {
        return setRetrofit().create(ApiKostumServices::class.java)
    }

    fun getOrderServices(): ApiOrderServices {
        return setRetrofit().create(ApiOrderServices::class.java)
    }

    fun getOrderItemServices(): ApiOrderItemServices {
        return setRetrofit().create(ApiOrderItemServices::class.java)
    }

    fun getUserServices(): ApiUserServices {
        return setRetrofit().create(ApiUserServices::class.java)
    }

    fun getLaporanServices(): ApiLaporanServices {
        return setRetrofit().create(ApiLaporanServices::class.java)
    }
}