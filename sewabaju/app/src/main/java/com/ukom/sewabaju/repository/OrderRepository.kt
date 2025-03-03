package com.ukom.sewabaju.repository

import android.view.View
import com.ukom.sewabaju.config.NetworkConfig
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.helper.RetrofitHelper
import com.ukom.sewabaju.model.Order
import com.ukom.sewabaju.model.request.OrderRequest

class OrderRepository(
    private val view: View
) {
    private val retrofit = NetworkConfig(view.context).getOrderServices()

    fun getOrders(
        orderStatus: String,
        namaPelanggan: String,
        onSuccess: (List<Order>) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.getOrders(orderStatus, namaPelanggan), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }

    fun insertOrder(
        orderRequest: OrderRequest,
        onSuccess: (Order) -> Unit,
        onFailure: () ->  Unit
    ) {
        RetrofitHelper.invoke(retrofit.insertOrder(orderRequest), {
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

    fun getOrder(
        orderId: Int,
        onSuccess: (Order) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.getOrder(orderId), {
            if (it == null) {
                MessageHelper.errorResult(view, "Terjadi gangguan")
                return@invoke
            }
            if (it.status) onSuccess(it.data) else MessageHelper.errorResult(view, it.message)
        }, {
            MessageHelper.errorResult(view, "Gagal menghubungi server")
        })
    }

    fun updateOrder(
        orderId: Int,
        orderRequest: OrderRequest,
        onSuccess: (Order) -> Unit,
        onFailure: () -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.updateOrder(orderId, orderRequest), {
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

    fun deleteOrder(
        orderId: Int,
        onSuccess: (Order) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.deleteOrder(orderId), {
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