package com.ukom.sewabaju.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ukom.sewabaju.databinding.ListSizeStockPriceBinding
import com.ukom.sewabaju.model.SizeStockPrice

class SizeStockPriceAdapter(
    private var items: MutableList<SizeStockPrice>,
    private var sync: (index: Int, data: SizeStockPrice) -> Unit,
    private var delete: (index: Int) -> Unit
): RecyclerView.Adapter<SizeStockPriceAdapter.SizeStockPriceViewHolder>() {
    class SizeStockPriceViewHolder(val binding: ListSizeStockPriceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeStockPriceViewHolder {
        val binding = ListSizeStockPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return SizeStockPriceViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SizeStockPriceViewHolder, position: Int) {
        val item = items[position]

        holder.binding.etSize.setText(item.size)
        holder.binding.etStock.setText(item.stock.toString())
        holder.binding.etPrice.setText(item.price.toString())

        holder.binding.btnDelete.setOnClickListener {
            delete(position)
        }

        holder.binding.etSize.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                item.size = holder.binding.etSize.text.toString()
            }
            sync(position, item)
        }

        holder.binding.etStock.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                item.stock = holder.binding.etStock.text.toString().toIntOrNull() ?: 0
            }
            sync(position, item)
        }

        holder.binding.etPrice.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                item.price = holder.binding.etPrice.text.toString().toIntOrNull() ?: 0
            }
            sync(position, item)
        }
    }

    fun updateData(items: MutableList<SizeStockPrice>) {
        this.items = items
        notifyDataSetChanged()
    }
}