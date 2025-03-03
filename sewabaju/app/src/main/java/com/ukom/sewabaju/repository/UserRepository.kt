package com.ukom.sewabaju.repository

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import com.ukom.sewabaju.R
import com.ukom.sewabaju.config.NetworkConfig
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.helper.RetrofitHelper
import com.ukom.sewabaju.model.User
import com.ukom.sewabaju.model.request.UserRequest
import com.ukom.sewabaju.model.response.APIResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class UserRepository(
    private val view: View
) {
    private val retrofit = NetworkConfig(view.context).getUserServices()

    fun updateUser(
        userRequest: UserRequest,
        onSuccess: (User) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.updateUser(userRequest), {
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

    fun getUserImage(
        imageName: String,
        onSuccess: (Bitmap) -> Unit
    ) {
        retrofit.getUserImage("${NetworkConfig(view.context).Base_URL.replace("api/", "")}${imageName}").enqueue(object:
            Callback<ResponseBody> {
            @SuppressLint("ResourceType")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var bitmap = BitmapFactory.decodeStream(view.context.resources.openRawResource(R.drawable.default_picture))
                if (!response.isSuccessful) {
                    return onSuccess(bitmap)
                }
                bitmap = response.body()?.byteStream()?.use {
                    BitmapFactory.decodeStream(it)
                }
                onSuccess(bitmap)
            }

            @SuppressLint("ResourceType")
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val bitmap = BitmapFactory.decodeStream(view.context.resources.openRawResource(R.drawable.default_picture))
                onSuccess(bitmap)
            }
        })
    }

    fun uploadImage(
        bitmap: Bitmap,
        onSuccess: (User) -> Unit
    ) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

        retrofit.uploadUserImage(multipartBody).enqueue(object: Callback<APIResponse<User>> {
            override fun onResponse(call: Call<APIResponse<User>>, response: Response<APIResponse<User>>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it.data) }
                }
            }

            override fun onFailure(call: Call<APIResponse<User>>, t: Throwable) {

            }
        })
    }
}