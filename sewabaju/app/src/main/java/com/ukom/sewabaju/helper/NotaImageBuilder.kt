package com.ukom.sewabaju.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.FileProvider
import com.ukom.sewabaju.model.Kostum
import com.ukom.sewabaju.model.Order
import com.ukom.sewabaju.repository.KostumRepository
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

interface OnNotaBitmapCreated {
    fun onBitmapCreated(bitmap: Bitmap)
}

class NotaImageBuilder(
    private val order: Order,
    private val kostumRepository: KostumRepository
) {
    private var y = 0f
    private lateinit var canvas: Canvas
    private lateinit var paintBold: Paint
    private lateinit var paintRegular: Paint
    fun createNotaBitmap(callback: OnNotaBitmapCreated) {
        val width = 400
        var height = 350
        for (item in order.orderItem!!) {
            height += 40
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val initCanvas = Canvas(bitmap)

        canvas = initCanvas

        canvas.drawColor(Color.WHITE)
        val initPaintBold = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            color = Color.BLACK
            textSize = 16f
        }
        val initPaintRegular = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            color = Color.BLACK
            textSize = 14f
        }
        y = 30f

        paintBold = initPaintBold
        paintRegular = initPaintRegular

        val parseDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        val user = order.user!!

        // add header
        y = addTextCenter(canvas, paintBold, "NOTA RENTAL", y)
        y = addDivider(canvas, paintRegular, y)
        // add body
        y = addTextStart(canvas, paintRegular, "Faktur\t\t\t\t\t\t\t\t\t\t\t:  ${order.faktur}", y)
        y = addTextStart(canvas, paintRegular, "Pelanggan\t:  ${user.name}", y)
        y = addTextStart(canvas, paintRegular, "Alamat\t\t\t\t\t\t\t\t\t:  ${user.address}", y)
        y = addTextStart(canvas, paintRegular, "Telepon\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t:  ${user.phoneNumber}", y)
        y = addTextStart(canvas, paintRegular, "Tanggal Awal\t\t\t:  ${formatDate.format(parseDate.parse(order.rentalStartDate)!!)}", y)
        y = addTextStart(canvas, paintRegular, "Tanggal Akhir\t\t\t:  ${formatDate.format(parseDate.parse(order.rentalEndDate)!!)}", y)

        y = when(order.orderStatus) {
            "pending" -> addTextStart(canvas, paintRegular, "Status:\t\t\t\t\t\t\t\t\t\t\t:  Proses", y)
            "confirmed" -> addTextStart(canvas, paintRegular, "Status:\t\t\t\t\t\t\t\t\t\t\t:  Dipinjam", y)
            "completed" -> addTextStart(canvas, paintRegular, "Status:\t\t\t\t\t\t\t\t\t\t\t:  Selesai", y)
            else -> addTextStart(canvas, paintRegular, "Status:\t\t\t\t\t\t\t\t\t\t\t:  Dibatalkan", y)
        }

        y = addDivider(canvas, paintRegular, y)
        val kostumIds = order.orderItem.map { it.kostumId }
        val kostums = mutableListOf<Kostum>()
        var fetchCount = 0

        kostumIds.forEach { kostumId ->
            kostumRepository.getKostumDetail(kostumId) { kostum ->
                kostums.add(kostum)
                fetchCount++

                if (fetchCount == kostumIds.size) {
                    addDetail(bitmap, kostums)
                    callback.onBitmapCreated(bitmap)
                }
            }
        }
    }

    private fun addDetail(bitmap: Bitmap, kostums: List<Kostum>): Bitmap {
        for (i in 0 until order.orderItem!!.size) {
            val kostum = kostums[i]
            val item = order.orderItem[i]

            y = addTextStart(canvas, paintRegular, "${kostum.name} - ${item.size}", y)
            val leftColumn = "${item.quantity} x ${item.price}"
            val subtotal = (item.quantity * item.price.toDouble()).toString()
            y = addTextColumns(canvas, paintRegular, leftColumn,subtotal,y);
        }

        y = addDivider(canvas, paintRegular, y)
        y = addTextColumns(canvas, paintRegular, "Subtotal: ", order.totalPrice, y)
        y = addTextColumns(canvas, paintRegular, "Total:", order.totalPrice, y)

        when(order.paymentStatus) {
            "paid" -> y = addTextColumns(canvas, paintRegular, "Status Pembayaran:","Sudah Dibayar", y)
            else -> y = addTextColumns(canvas, paintRegular, "Status Pembayaran:", "Belum Dibayar", y)
        }

        y = addEmptyLine(canvas, paintRegular, y)
        y = addTextCenter(canvas, paintRegular, "Terima Kasih", y)

        return bitmap
    }

    fun addEmptyLine(canvas: Canvas, paint: Paint, y: Float): Float {
        return y + paint.fontSpacing
    }

    fun addTextStart(canvas: Canvas, paint: Paint, text: String, y: Float) : Float {
        val x = 20f
        canvas.drawText(text, x, y, paint)
        return y + paint.fontSpacing + 2
    }
    fun addTextCenter(canvas: Canvas, paint: Paint, text: String, y: Float) : Float {
        val x = (canvas.width - paint.measureText(text)) / 2
        canvas.drawText(text, x, y, paint)
        return y + paint.fontSpacing + 4
    }

    fun addTextColumns(canvas: Canvas, paint: Paint, column1: String, column2: String, y: Float):Float {
        val startX = 20f
        val endX = canvas.width - paint.measureText(column2) - 20

        canvas.drawText(column1, startX, y, paint)

        canvas.drawText(column2, endX, y, paint)
        return y + paint.fontSpacing + 2
    }

    fun addDivider(canvas: Canvas, paint: Paint, y: Float): Float {
        val dashLine:Paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 1f
            pathEffect = DashPathEffect(floatArrayOf(4f, 4f), 0f)
        }
        paint.strokeWidth = 2f
        canvas.drawLine(20f, y, canvas.width - 20f, y, dashLine)
        return y + 20f
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
        val file = File(context.cacheDir, "receipt.png")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        val packageApp: String = context.packageName + ".fileprovider"
        val contentUri = FileProvider.getUriForFile(context, packageApp, file)
        return contentUri
    }

    fun shareReceiptViaWhatsApp(context: Context,receiptUri: Uri) {
        val phone = order.user!!.phoneNumber.replaceFirst("^08".toRegex(), "628")
        val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, receiptUri)
            putExtra("jid", "$phone@s.whatsapp.net")
            putExtra(Intent.EXTRA_TEXT, "Terima kasih sudah melakukan order, berikut adalah nota transaksi Anda")
            setPackage("com.whatsapp")
        }
        context.startActivity(Intent.createChooser(whatsappIntent, "Share receipt"))
    }

    fun generateAndShareReceipt(context: Context) {
        createNotaBitmap(object : OnNotaBitmapCreated {
            override fun onBitmapCreated(bitmap: Bitmap) {
                val receiptUri = saveBitmapToFile(context, bitmap)
                shareReceiptViaWhatsApp(context, receiptUri)
            }
        })
    }
}