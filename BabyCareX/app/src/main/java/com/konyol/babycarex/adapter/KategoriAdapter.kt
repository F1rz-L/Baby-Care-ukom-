package com.konyol.babycarex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.konyol.babycarex.databinding.ListKategoriAdapterBinding
import com.konyol.babycarex.data.model.Kategori

class KategoriAdapter(
    private val kategoriList: MutableList<Kategori>,  // Change to MutableList for easy modification
    private val onEditClick: (Kategori) -> Unit,
    private val onDeleteClick: (Kategori) -> Unit
) : RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder>() {

    inner class KategoriViewHolder(val binding: ListKategoriAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val binding = ListKategoriAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KategoriViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        val kategori = kategoriList[position]
        holder.binding.apply {
            tvKategori.text = kategori.kategori

            btnEdit.setOnClickListener {
                onEditClick(kategori)
            }

            // Remove from the list in the adapter if needed (from the `onDeleteClick` handler):
            btnDelete.setOnClickListener {
                val context = holder.itemView.context
                val builder = AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this category?")
                    .setPositiveButton("Yes") { _, _ ->
                        onDeleteClick(kategori)  // Call deleteKategori from activity
                    }
                    .setNegativeButton("No", null)
                builder.show()
            }

        }
    }

    override fun getItemCount(): Int = kategoriList.size
}

