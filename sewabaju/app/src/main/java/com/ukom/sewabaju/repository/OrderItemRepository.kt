package com.ukom.sewabaju.repository

import android.view.View
import com.ukom.sewabaju.config.NetworkConfig
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.helper.RetrofitHelper
import com.ukom.sewabaju.model.OrderItem
import com.ukom.sewabaju.model.request.OrderItemRequest

class OrderItemRepository(
    private val view: View
) {
    private val retrofit = NetworkConfig(view.context).getOrderItemServices()

    fun insertOrderItem(
        orderItemRequest: OrderItemRequest,
        onSuccess: (OrderItem) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.insertOrderItem(orderItemRequest), {
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