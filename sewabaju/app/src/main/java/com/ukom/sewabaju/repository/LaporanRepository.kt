package com.ukom.sewabaju.repository

import android.view.View
import com.ukom.sewabaju.config.NetworkConfig
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.helper.RetrofitHelper
import com.ukom.sewabaju.model.response.Laporan

class LaporanRepository(
    private val view: View
) {
    private val retrofit = NetworkConfig(view.context).getLaporanServices()

    fun getLaporan(
        startDate: String,
        endDate: String,
        onSuccess: (Laporan) -> Unit
    ) {
        RetrofitHelper.invoke(retrofit.getLaporan(startDate, endDate), {
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