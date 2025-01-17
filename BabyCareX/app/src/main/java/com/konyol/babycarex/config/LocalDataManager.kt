package com.konyol.babycarex.config

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "babycare_prefs"
        private const val CACHEPREFS_NAME = "cache_api"
        private const val KEY_TOKEN = "api_token"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    private val cacheSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(CACHEPREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun setFloat(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun clearLastSync() {
        cacheSharedPreferences.edit().clear().apply()
    }


    fun setToken(token: String) {
        setString(KEY_TOKEN, token)
    }

    fun getToken(): String? {
        return getString(KEY_TOKEN)
    }

    fun clearToken() {
        sharedPreferences.edit().clear().apply()
    }

    fun isUserVerifying(): Boolean {
        return sharedPreferences.getBoolean("verifying", false)
    }

    fun setUserVerifying(state: Boolean) {
        sharedPreferences.edit().putBoolean("verifying", state).apply()
    }
}
