package com.ukom.sewabaju.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ukom.sewabaju.databinding.ListOrderBinding
import com.ukom.sewabaju.model.Order
import java.text.SimpleDateFormat
import java.util.Locale

class OrderAdapter(
    private val items: List<Order>,
    private val callback: (id: Int) -> Unit
): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    class OrderViewHolder(val binding: ListOrderBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ListOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = items[position]

        val dateParse = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        holder.binding.startDate = dateFormat.format(dateParse.parse(item.rentalStartDate)!!)
        holder.binding.endDate = dateFormat.format(dateParse.parse(item.rentalEndDate)!!)
        holder.binding.status = item.orderStatus
        holder.binding.quantity = item.orderItem!!.sumOf { it.quantity }.toString()
        holder.binding.total = item.totalPrice

        holder.binding.root.setOnClickListener {
            callback(item.id)
        }
    }
}