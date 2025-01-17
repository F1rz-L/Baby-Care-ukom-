package com.konyol.babycarex.activity.kasir

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.konyol.babycarex.R
import com.konyol.babycarex.data.model.Barang
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.data.network.PelangganApiService
import com.konyol.babycarex.data.network.PinjamanApiService
import com.konyol.babycarex.databinding.ActivityPenyewaanBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PenyewaanActivity : AppCompatActivity() {
    @Inject lateinit var barangApiService: BarangApiService
    @Inject lateinit var pinjamanApiService: PinjamanApiService
    @Inject lateinit var pelangganApiService: PelangganApiService

    private lateinit var binding: ActivityPenyewaanBinding
    private var barangId: Int = 0
    private var barang: Barang? = null
    private val pelangganMap = mutableMapOf<String, Int>() // Maps pelanggan names to IDs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPenyewaanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        barangId = intent.getIntExtra("barangId", 0)
        if (barangId == 0) {
            Toast.makeText(this, "ID barang tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchBarangDetails()
        fetchPelangganList()

        setupListeners()
    }

    private fun fetchBarangDetails() {
        lifecycleScope.launch {
            try {
                val response = barangApiService.getBarangById(barangId)
                if (response.isSuccessful && response.body() != null) {
                    barang = response.body()!!.data
                    binding.tvNama.text = barang?.namabarang ?: "Unknown"
                    binding.tvMerk.text = barang?.merk ?: "Unknown"
                } else {
                    handleError("Failed to fetch barang details: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("An error occurred while fetching barang details: ${e.message}")
            }
        }
    }

    private fun fetchPelangganList() {
        lifecycleScope.launch {
            try {
                val response = pelangganApiService.getAllPelanggan()
                if (response.isSuccessful && response.body() != null) {
                    val pelangganList = response.body()!!.data
                    pelangganList.forEach { pelanggan ->
                        pelangganMap[pelanggan.nama] = pelanggan.id!!.toInt()
                    }
                    val pelangganNames = pelangganMap.keys.toList()
                    val adapter = ArrayAdapter(
                        this@PenyewaanActivity,
                        R.layout.dropdown_item,
                        pelangganNames
                    )
                    adapter.setDropDownViewResource(R.layout.dropdown_item)
                    binding.edtPelanggan.setAdapter(adapter)
                } else {
                    handleError("Failed to fetch pelanggan: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("An error occurred while fetching pelanggan: ${e.message}")
            }
        }
    }

    private fun setupListeners() {
        // Update total price and minggu count on text change
        binding.edtMingguSewa.addTextChangedListener(afterTextChanged = { s ->
            val lamaSewa = s.toString().toIntOrNull() ?: 0
            val harga = barang?.harga ?: 0
            val totalHarga = lamaSewa * harga

            val formattedMinggu = if (lamaSewa > 0) "(${lamaSewa} minggu)" else ""
            val formattedHarga = "Rp${String.format("%,d", totalHarga)}".replace(",", ".")

            binding.tvRincianHarga.text = formattedHarga
            binding.mingguCount.text = formattedMinggu
        })

        // Handle rent button click
        binding.btnSewa.setOnClickListener {
            val lamaSewa = binding.edtMingguSewa.text.toString().toIntOrNull()
            val pelangganName = binding.edtPelanggan.text.toString()

            if (lamaSewa == null || lamaSewa <= 0) {
                Toast.makeText(this, "Lama sewa harus diisi dengan benar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idPelanggan = pelangganMap[pelangganName]
            if (idPelanggan == null) {
                Toast.makeText(this, "Pelanggan tidak valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sewaBarang(barangId, lamaSewa, idPelanggan)
        }
    }

    private fun sewaBarang(barangId: Int, lamaSewa: Int, idPelanggan: Int) {
        lifecycleScope.launch {
            try {
                val response = pinjamanApiService.addPinjaman(idPelanggan, barangId, lamaSewa)
                if (response.isSuccessful) {
                    Toast.makeText(this@PenyewaanActivity, "Barang berhasil disewa!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    handleError("Failed to rent barang: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("An error occurred while renting barang: ${e.message}")
            }
        }
    }

    private fun handleError(message: String) {
        Log.e("PenyewaanActivity", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
