package com.konyol.babycarex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.konyol.babycarex.data.model.Barang
import com.konyol.babycarex.data.model.Pelanggan
import com.konyol.babycarex.databinding.ListPinjamanAdapterBinding
import com.konyol.babycarex.data.model.Pinjaman
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.data.network.PelangganApiService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PinjamanAdapter(
    private val pinjamanList: MutableList<Pinjaman>,  // Change to MutableList for easy modification
    private val barangApiService: BarangApiService,
    private val pelangganApiService: PelangganApiService,
    private val lifecycleOwner: LifecycleOwner, // Pass the LifecycleOwner explicitly
    private val onReturnClick: (Int) -> Unit
) : RecyclerView.Adapter<PinjamanAdapter.PinjamanViewHolder>() {
    inner class PinjamanViewHolder(val binding: ListPinjamanAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinjamanViewHolder {
        val binding =ListPinjamanAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PinjamanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PinjamanViewHolder, position: Int) {
        val pinjaman = pinjamanList[position]
        holder.binding.apply {
            // Set initial placeholder text
            tvBarang.text = "Fetching..."
            tvPelanggan.text = "Fetching..."


            // Fetch barang name
            fetchBarangName(pinjaman.id_barang, tvBarang, holder)

            // Fetch pelanggan name
            fetchPelangganName(pinjaman.id_peminjam, tvPelanggan, holder)

            tvTanggalPinjam.text = pinjaman.tgl_pinjam
            tvTanggalKembali.text = pinjaman.tgl_kembali
            tvDenda.text = "Rp${pinjaman.denda}"
            when (pinjaman.status) {
                "Dipinjam" -> {
                    btnReturn.visibility = View.VISIBLE
                    imgTick.visibility = View.GONE
                }
                "Dikembalikan" -> {
                    btnReturn.visibility = View.GONE
                    imgTick.visibility = View.VISIBLE
                }
            }

            btnReturn.setOnClickListener {
                val context = holder.itemView.context
                val builder = AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to return this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        onReturnClick(pinjaman.id!!.toInt())
                    }
                    .setNegativeButton("No", null)
                builder.show()
            }
        }
    }

    override fun getItemCount(): Int = pinjamanList.size

    // Helper method to fetch barang name
    private fun fetchBarangName(idBarang: Int, textView: TextView, holder: PinjamanViewHolder) {
        lifecycleOwner.lifecycleScope.launch {
            try {
                val response = barangApiService.getBarangById(idBarang).body()?.data
                textView.text = response?.namabarang ?: "Unknown"
            } catch (e: Exception) {
                textView.text = "Failed to fetch barang"
            }
        }
    }

    // Helper method to fetch pelanggan name
    private fun fetchPelangganName(idPelanggan: Int, textView: TextView, holder: PinjamanViewHolder) {
        lifecycleOwner.lifecycleScope.launch {
            try {
                val response = pelangganApiService.getPelangganById(idPelanggan).body()?.data
                textView.text = response?.nama ?: "Unknown"
            } catch (e: Exception) {
                textView.text = "Failed to fetch pelanggan"
            }
        }
    }
}

