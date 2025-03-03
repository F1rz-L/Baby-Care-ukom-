package com.ukom.sewabaju.adapter.recyclerView

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ukom.sewabaju.databinding.ListOrderItemBinding
import com.ukom.sewabaju.model.OrderItem
import com.ukom.sewabaju.repository.KostumRepository

class OrderItemAdapter(
    private val items: List<OrderItem>,
    private val kostumRepository: KostumRepository
): RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {
    class OrderItemViewHolder(val binding: ListOrderItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding = ListOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return OrderItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = items[position]

        holder.binding.size = item.size
        holder.binding.price = item.price
        holder.binding.amount = item.quantity.toString()
        holder.binding.subtotal = (item.price.toDouble() * item.quantity).toString()

        kostumRepository.getKostumDetail(item.kostumId) { kostum ->
            holder.binding.kategori = kostum.category?.name
            holder.binding.name = kostum.name

            kostumRepository.getKostumImage(kostum.image) { bitmap ->
                holder.binding.image = BitmapDrawable(holder.binding.root.resources, bitmap)
            }
        }
    }
}