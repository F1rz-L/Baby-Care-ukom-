package com.konyol.babycarex.data

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.konyol.babycarex.R

object MessageHelper {
    fun customAlert(view: View, title: String, message: String, onPositive: () -> Unit, onNegative: (dialogId: Int) -> Unit) {
        AlertDialog.Builder(view.context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ya") { _, _ -> onPositive() }
            .setNegativeButton("Tidak") { _, dialogId -> onNegative(dialogId) }
            .create()
            .show()
    }

    fun successResult(view: View, message: String) {
        if (message.isEmpty()) return

        val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(view.context.resources.getColor(R.color.SecondaryColor))
        snackbar.setAction("Close") {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    fun errorResult(view: View, message: String) {
        if (message.isEmpty()) return

        val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(view.context.resources.getColor(R.color.red))
        snackbar.setAction("Close") {
            snackbar.dismiss()
        }
        snackbar.show()
    }
}