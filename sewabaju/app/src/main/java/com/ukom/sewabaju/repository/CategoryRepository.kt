package com.ukom.sewabaju.repository

import android.view.View
import com.ukom.sewabaju.config.NetworkConfig
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.helper.RetrofitHelper
import com.ukom.sewabaju.model.Category
import com.ukom.sewabaju.model.request.CategoryRequest

class CategoryRepository(
    val view: View
) {
    private val retrofit = NetworkConfig(view.context).getCategoryServices()

    fun getCategories(
        onSuccess: (List<Category>) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.getCategories(), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }

    fun insertCategory(
        categoryRequest: CategoryRequest,
        onSuccess: (Category) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.insertCategory(categoryRequest), {
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

    fun getCategoryDetail(
        categoryId: Int,
        onSuccess: (Category) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.getCategoryDetail(categoryId), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }

    fun updateCategory(
        categoryId: Int,
        categoryRequest: CategoryRequest,
        onSuccess: (Category) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.updateCategory(categoryId, categoryRequest), {
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

    fun deleteCategory(
        categoryId: Int,
        onSuccess: (Category) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.deleteCategory(categoryId), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }
}