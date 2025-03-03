package com.konyol.babycarex.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.Debug
import androidx.recyclerview.widget.RecyclerView
import com.konyol.babycarex.R
import com.konyol.babycarex.databinding.ListBarangKasirAdapterBinding
import com.konyol.babycarex.activity.kasir.ListBarangModel
import com.konyol.babycarex.activity.kasir.PenyewaanActivity
import com.konyol.babycarex.data.di.NetworkModule
import com.konyol.babycarex.data.model.Barang
import com.squareup.picasso.Picasso

class BarangKasirAdapter(
    private val itemList: MutableList<Barang>
) : RecyclerView.Adapter<BarangKasirAdapter.BarangViewHolder>() {

    class BarangViewHolder(val binding: ListBarangKasirAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        // Inflate the layout using binding
        val binding = ListBarangKasirAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BarangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val item = itemList[position]

        // Access views directly through binding
        holder.binding.apply {
            nama.text = item.namabarang
            tvMerk.text = item.merk
            tvKategori.text = item.id_kategori.toString()
            harga.text = "Rp" + item?.harga.toString()

            if (item?.gambar !== null) {
                val imgUrl = NetworkModule.BASE_IMG_URL + item?.gambar
                Picasso.get().load(imgUrl).into(gambar)
                Log.d("BarangKasirAdapter" , "Gambar URL: $imgUrl")
            }

            when (item?.status) {
                "Dipinjam" -> {
                    btnSewa.visibility = View.GONE
                    btnDipinjam.visibility = View.VISIBLE
                }

                "Tidak Tersedia" -> {
                    btnSewa.visibility = View.GONE
                    btnTidakTersedia.visibility = View.VISIBLE
                }
            }

            // Optional: Add a click listener for the "Sewa" button
            btnSewa.setOnClickListener {
                val intent = Intent(holder.itemView.context, PenyewaanActivity::class.java)
                intent.putExtra("barangId", item.id) // Pass the barangId to the next activity
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = itemList.size
}
