package com.ukom.sewabaju.adapter.recyclerView

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ukom.sewabaju.databinding.ListCartBinding
import com.ukom.sewabaju.repository.KostumRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.addCart
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.removeCart
import org.json.JSONArray

class CartAdapter(
    private val items: JSONArray,
    private val kostumRepository: KostumRepository,
    private val loadData: () -> Unit,
    private val callback: (id: Int) -> Unit
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(val binding: ListCartBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ListCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = items.length()

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items.getJSONObject(position)

        holder.binding.name = item.getString("name")
        holder.binding.price = item.getString("price")
        holder.binding.size = item.getString("size")
        holder.binding.subTotal = item.getInt("subTotal").toString()

        var amount = item.getInt("amount")
        val stock = item.getInt("stock")

        holder.binding.amount = amount.toString()

        holder.binding.root.setOnClickListener {
            callback(item.getInt("id"))
        }

        kostumRepository.getKostumImage(item.getString("image")) { bitmap ->
            holder.binding.image = BitmapDrawable(holder.binding.root.resources, bitmap)
        }

        holder.binding.btnMin.setOnClickListener {
            --amount
            val modifiedItem = item.put("amount", -1)
            if (amount <= 0) {
                holder.binding.root.context.removeCart(item)
            } else {
                holder.binding.root.context.addCart(modifiedItem)
            }
            holder.binding.amount = amount.toString()
            loadData()
        }

        holder.binding.btnPlus.setOnClickListener {
            ++amount
            val modifiedItem = item.put("amount", 1)
            if (amount <= stock) {
                holder.binding.root.context.addCart(modifiedItem)
            }
            holder.binding.amount = amount.toString()
            loadData()
        }
    }

}