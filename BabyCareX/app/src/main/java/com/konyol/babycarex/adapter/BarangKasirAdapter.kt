package com.konyol.babycarex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.konyol.babycarex.R
import com.konyol.babycarex.databinding.ListBarangAdapterBinding
import com.konyol.babycarex.activity.kasir.ListBarangModel
import com.squareup.picasso.Picasso

class BarangKasirAdapter(
    private val itemList: List<ListBarangModel>
) : RecyclerView.Adapter<BarangKasirAdapter.BarangViewHolder>() {

    class BarangViewHolder(val binding: ListBarangAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        // Inflate the layout using binding
        val binding = ListBarangAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val item = itemList[position]

        // Access views directly through binding
        holder.binding.apply {
            nama.text = item?.nama
            merk.text = item?.merk
            kategori.text = item?.kategori
            Picasso.get().load(item.gambar).into(gambar)
            harga.text = item?.harga

            // Optional: Add a click listener for the "Sewa" button
            btnSewa.setOnClickListener {
                // Handle click event for the "Sewa" button here
            }
        }
    }

    override fun getItemCount() = itemList.size
}
