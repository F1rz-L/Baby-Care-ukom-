package com.konyol.babycarex.data.di

import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.data.network.KategoriApiService
import com.konyol.babycarex.data.network.PelangganApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://192.168.1.2:8000/api/" // Replace with your base URL

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKategoriApiService(retrofit: Retrofit): KategoriApiService {
        return retrofit.create(KategoriApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePelangganApiService(retrofit: Retrofit): PelangganApiService {
        return retrofit.create(PelangganApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBarangApiService(retrofit: Retrofit): BarangApiService {
        return retrofit.create(BarangApiService::class.java)
    }
}
