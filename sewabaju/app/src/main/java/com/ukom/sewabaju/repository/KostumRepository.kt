package com.ukom.sewabaju.repository

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import com.ukom.sewabaju.R
import com.ukom.sewabaju.config.NetworkConfig
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.helper.RetrofitHelper
import com.ukom.sewabaju.model.Kostum
import com.ukom.sewabaju.model.request.KostumRequest
import com.ukom.sewabaju.model.response.APIResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class KostumRepository(
    private val view: View
) {
    private val retrofit = NetworkConfig(view.context).getKostumServices()

    fun getKostumByCategory(
        categoryId: Int,
        search: String,
        onSuccess: (List<Kostum>) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.getKostumByCategory(categoryId, search), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }

    fun insertKostum(
        kostumRequest: KostumRequest,
        onSuccess: (Kostum) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.insertKostum(kostumRequest), {
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

    fun getKostumDetail(
        kostumId: Int,
        onSuccess: (Kostum) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.getKostumDetail(kostumId), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }

    fun updateKostum(
        kostumId: Int,
        kostumRequest: KostumRequest,
        onSuccess: (Kostum) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.updateKostum(kostumId, kostumRequest), {
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

    fun deleteKostum(
        kostumId: Int,
        onSuccess: (Kostum) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.deleteKostum(kostumId), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }

    fun getKostumImage(
        imageName: String,
        onSuccess: (Bitmap) -> Unit
    ) {
        retrofit.getKostumImage("${NetworkConfig(view.context).Base_URL.replace("api/", "")}${imageName}").enqueue(object: Callback<ResponseBody> {
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
        id: Int,
        bitmap: Bitmap,
        onSuccess: (Kostum) -> Unit
    ) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

        retrofit.uploadKostumImage(id, multipartBody).enqueue(object: Callback<APIResponse<Kostum>> {
            override fun onResponse(call: Call<APIResponse<Kostum>>, response: Response<APIResponse<Kostum>>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it.data) }
                }
            }

            override fun onFailure(call: Call<APIResponse<Kostum>>, t: Throwable) {

            }
        })
    }
}