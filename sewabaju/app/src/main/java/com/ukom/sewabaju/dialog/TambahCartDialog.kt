package com.ukom.sewabaju.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.ukom.sewabaju.databinding.DialogTambahCartBinding
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.model.Kostum
import com.ukom.sewabaju.repository.KostumRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.addCart
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.getCartItemAmount
import org.json.JSONObject

class TambahCartDialog(
    private val view: View,
    private val kostum: Kostum,
    private val kostumRepository: KostumRepository
): Dialog(view.context) {
    private val binding: DialogTambahCartBinding = DialogTambahCartBinding.inflate(LayoutInflater.from(view.context))
    private var selectedUnit = kostum.sizeStockPrice[0]
    private var amount = 0

    init {
        setContentView(binding.root)

        initLayout()
        handleEvent()
        refreshData()
        window?.apply {
            setLayout(
                (WindowManager.LayoutParams.MATCH_PARENT),
                (WindowManager.LayoutParams.WRAP_CONTENT)
            )
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

    private fun initLayout() {
        val sizeList = kostum.sizeStockPrice.map { it.size }
        binding.autoCompleteUkuran.apply {
            setAdapter(ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, sizeList))
            setOnItemClickListener { _, _, position, _ ->
                selectedUnit = kostum.sizeStockPrice[position]
                refreshData()
            }
            setText(selectedUnit.size, false)
        }
        kostumRepository.getKostumImage(kostum.image) { bitmap ->
            binding.image = BitmapDrawable(binding.root.resources, bitmap)
        }
    }

    private fun handleEvent() {
        binding.btnMin.setOnClickListener {
            if (amount >= 0) --amount
            binding.etAmount.setText(amount.toString())
        }
        binding.btnPlus.setOnClickListener {
            if (amount <= selectedUnit.stock!!) ++amount
            binding.etAmount.setText(amount.toString())
        }
        binding.etAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                binding.etAmount.removeTextChangedListener(this)

                if (binding.etAmount.text.toString().isEmpty()) {
                    binding.etAmount.addTextChangedListener(this)
                    return
                } else {
                    amount = try {
                        binding.etAmount.text.toString().toInt()
                    } catch (e: NumberFormatException) {
                        0
                    }

                    if (amount < 0) amount = 0
                    if (amount > selectedUnit.stock!!) amount = selectedUnit.stock!!
                }

                binding.etAmount.setText(amount.toString())
                binding.etAmount.setSelection(binding.etAmount.text.toString().length)

                binding.etAmount.addTextChangedListener(this)
            }
        })
        binding.btnConfirm.setOnClickListener {
            addToCart()
        }
    }

    private fun refreshData() {
        amount = 0
        binding.etAmount.setText(amount.toString())

        binding.price = selectedUnit.price.toString()
        binding.stok = selectedUnit.stock.toString()
    }

    private fun addToCart() {
        if (amount == 0) {
            dismiss()
            return MessageHelper.errorResult(view, "Jumlah kostum harus diisi")
        }

        val cartAmount = view.context.getCartItemAmount(kostum.id, selectedUnit.size)
        if (cartAmount != null && selectedUnit.stock!! < cartAmount + amount) {
            dismiss()
            return MessageHelper.errorResult(view, "Jumlah item yang telah dimasukkan keranjang melebihi stok")
        }

        val json = JSONObject().apply {
            put("id", kostum.id)
            put("name", kostum.name)
            put("size", selectedUnit.size)
            put("amount", amount)
            put("stock", selectedUnit.stock)
            put("price", selectedUnit.price)
            put("subTotal", selectedUnit.price?.times(amount) ?: 0)
            put("image", kostum.image)
        }

        view.context.addCart(json)

        MessageHelper.successResult(view, "Sukses menambahkan kostum ke keranjang")
        dismiss()
    }
}