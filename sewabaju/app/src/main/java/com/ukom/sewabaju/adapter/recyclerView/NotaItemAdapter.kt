package com.ukom.sewabaju.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ukom.sewabaju.databinding.ListNotaItemBinding
import com.ukom.sewabaju.model.OrderItem
import com.ukom.sewabaju.repository.KostumRepository

class NotaItemAdapter(
    private val items: List<OrderItem>,
    private val kostumRepository: KostumRepository
): RecyclerView.Adapter<NotaItemAdapter.NotaItemViewHolder>() {
    class NotaItemViewHolder(val binding: ListNotaItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaItemViewHolder {
        val binding = ListNotaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return NotaItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: NotaItemViewHolder, position: Int) {
        val item = items[position]

        kostumRepository.getKostumDetail(item.kostumId) {
            holder.binding.kostum = "${it.name} - ${item.size}"
        }

        holder.binding.jumlah = "${item.quantity} x ${item.price}"
        holder.binding.subtotal = (item.quantity * item.price.toDouble()).toString()
    }
}