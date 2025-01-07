package com.konyol.babycarex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.konyol.babycarex.databinding.ListPelangganAdapterBinding
import com.konyol.babycarex.data.model.Pelanggan

class PelangganAdapter(
    private val pelangganList: MutableList<Pelanggan>,  // Change to MutableList for easy modification
    private val onEditClick: (Pelanggan) -> Unit,
    private val onDeleteClick: (Pelanggan) -> Unit
) : RecyclerView.Adapter<PelangganAdapter.PelangganViewHolder>() {

    inner class PelangganViewHolder(val binding: ListPelangganAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PelangganViewHolder {
        val binding = ListPelangganAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PelangganViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PelangganViewHolder, position: Int) {
        val pelanggan = pelangganList[position]
        holder.binding.apply {
            tvNama.text = pelanggan.nama
            tvAlamat.text = pelanggan.alamat
            tvNoTelp.text = pelanggan.nomor_telepon
            tvNIK.text = pelanggan.nik

            btnEdit.setOnClickListener {
                onEditClick(pelanggan)
            }

            // Remove from the list in the adapter if needed (from the `onDeleteClick` handler):
            btnDelete.setOnClickListener {
                val context = holder.itemView.context
                val builder = AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this category?")
                    .setPositiveButton("Yes") { _, _ ->
                        onDeleteClick(pelanggan)  // Call deletePelanggan from activity
                    }
                    .setNegativeButton("No", null)
                builder.show()
            }

        }
    }

    override fun getItemCount(): Int = pelangganList.size
}

