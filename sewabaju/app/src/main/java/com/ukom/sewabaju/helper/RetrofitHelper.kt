package com.ukom.sewabaju.helper

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RetrofitHelper {
    operator fun <T> invoke(call: Call<T>, onSuccess: (T?) -> Unit, onFailure: (Throwable) -> Unit) {
        call.enqueue(object: Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                Log.e("RetrofitHelperSuccess", response.toString())
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onSuccess(null)
                }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.e("RetrofitHelperError", t.toString())
                onFailure(t)
            }
        })
    }
}