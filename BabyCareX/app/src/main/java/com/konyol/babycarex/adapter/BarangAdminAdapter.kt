package com.konyol.babycarex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.konyol.babycarex.databinding.ListBarangAdminAdapterBinding
import com.konyol.babycarex.data.model.Barang

class BarangAdminAdapter(
    private val barangList: MutableList<Barang>,  // Change to MutableList for easy modification
    private val onEditClick: (Barang) -> Unit,
    private val onDeleteClick: (Barang) -> Unit
) : RecyclerView.Adapter<BarangAdminAdapter.BarangViewHolder>() {

    inner class BarangViewHolder(val binding: ListBarangAdminAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val binding = ListBarangAdminAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val barang = barangList[position]
        holder.binding.apply {
            tvBarang.text = barang.namabarang
            tvMerk.text = barang.merk
            tvKategori.text = barang.id_kategori.toString()
            tvHarga.text = barang.harga.toString()

            btnEdit.setOnClickListener {
                onEditClick(barang)
            }

            // Remove from the list in the adapter if needed (from the `onDeleteClick` handler):
            btnDelete.setOnClickListener {
                val context = holder.itemView.context
                val builder = AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this category?")
                    .setPositiveButton("Yes") { _, _ ->
                        onDeleteClick(barang)  // Call deleteBarang from activity
                    }
                    .setNegativeButton("No", null)
                builder.show()
            }

        }
    }

    override fun getItemCount(): Int = barangList.size
}

