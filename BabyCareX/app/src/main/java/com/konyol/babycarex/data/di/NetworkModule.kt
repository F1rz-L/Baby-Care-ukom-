package com.konyol.babycarex.data.di

import android.content.Context
import com.konyol.babycarex.config.AuthInterceptor
import com.konyol.babycarex.config.LocalDataManager
import com.konyol.babycarex.data.network.AuthApiService
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.data.network.KategoriApiService
import com.konyol.babycarex.data.network.PelangganApiService
import com.konyol.babycarex.data.network.PinjamanApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_THING = "http://192.168.187.58:8000"

    private const val BASE_URL = "$BASE_THING/api/" // Replace with your base URL
    const val BASE_IMG_URL = "$BASE_THING/storage/"

    @Provides
    fun provideAuthInterceptor(localDataManager: LocalDataManager, @ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(localDataManager, context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Injected AuthInterceptor
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Replace with your base URL
            .client(okHttpClient)
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
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePinjamanApiService(retrofit: Retrofit): PinjamanApiService {
        return retrofit.create(PinjamanApiService::class.java)
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
