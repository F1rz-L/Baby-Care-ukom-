package com.ukom.sewabaju.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.ukom.sewabaju.R
import com.ukom.sewabaju.databinding.DialogOptionBinding

class OptionDialog(
    private val context: Context,
    private val navigateTambahKostum: () -> Unit,
    private val navigateTambahCategory: () -> Unit
): Dialog(context, R.style.FullScreenDialog) {
    private val binding: DialogOptionBinding = DialogOptionBinding.inflate(LayoutInflater.from(this.context))

    init {
        setContentView(binding.root)

        handleEvent()
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.BOTTOM or Gravity.END
        layoutParams.y = 400

        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes = layoutParams
        }
    }

    private fun handleEvent() {
        binding.btnTambahKostum.setOnClickListener {
            navigateTambahKostum()
            dismiss()
        }
        binding.btnTambahCategory.setOnClickListener {
            navigateTambahCategory()
            dismiss()
        }
    }
}