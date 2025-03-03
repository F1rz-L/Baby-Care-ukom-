package com.ukom.sewabaju.adapter.recyclerView

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ukom.sewabaju.databinding.ListKostumBinding
import com.ukom.sewabaju.model.Kostum
import com.ukom.sewabaju.repository.KostumRepository

class CustomerAdapter(
    private val items: List<Kostum>,
    private val kostumRepository: KostumRepository,
    private val callback: (id: Int) -> Unit
): RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {
    class CustomerViewHolder(val binding: ListKostumBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding = ListKostumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return CustomerViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val item = items[position]

        holder.binding.name = item.name
        holder.binding.price = item.sizeStockPrice[0].price.toString()
        holder.binding.size = item.sizeStockPrice[0].size

        holder.binding.root.setOnClickListener {
            callback(item.id)
        }

        kostumRepository.getKostumImage(item.image) { bitmap ->
            holder.binding.image = BitmapDrawable(holder.binding.root.resources, bitmap)
        }
    }

}