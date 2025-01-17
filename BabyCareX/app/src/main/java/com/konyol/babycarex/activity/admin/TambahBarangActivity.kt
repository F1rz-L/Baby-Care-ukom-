package com.konyol.babycarex.activity.admin

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.konyol.babycarex.R
import com.konyol.babycarex.data.model.Barang
import com.konyol.babycarex.data.model.Pelanggan
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.data.network.KategoriApiService
import com.konyol.babycarex.data.network.PelangganApiService
import com.konyol.babycarex.databinding.ActivityTambahBarangBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TambahBarangActivity : AppCompatActivity() {
    @Inject
    lateinit var barangApiService: BarangApiService
    @Inject
    lateinit var kategoriApiService: KategoriApiService

    private val kategoriMap = mutableMapOf<String, Int>() // Maps category names to IDs
    private lateinit var binding: ActivityTambahBarangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Fetch and populate dropdown
        fetchKategoriList()

        // Handle "Add Barang" button click
        binding.btnTmbhBarang.setOnClickListener {
            validateAndAddBarang()
        }
    }

    /**
     * Fetches the category list from the API and populates the dropdown.
     */
    private fun fetchKategoriList() {
        lifecycleScope.launch {
            try {
                val response = kategoriApiService.getAllKategori()
                if (response.isSuccessful && response.body() != null) {
                    val kategoriList = response.body()!!.data
                    kategoriList.forEach { kategori ->
                        kategoriMap[kategori.kategori] = kategori.id!!.toInt()
                    }
                    val kategoriNames = kategoriMap.keys.toList()
                    val adapter = ArrayAdapter(
                        this@TambahBarangActivity,
                        R.layout.dropdown_item,
                        kategoriNames
                    )
                    adapter.setDropDownViewResource(R.layout.dropdown_item)
                    binding.edtKategori.setAdapter(adapter)
                } else {
                    handleError("Failed to fetch categories: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("An error occurred while fetching categories: ${e.message}")
            }
        }
    }

    /**
     * Validates the input fields and initiates adding a new item.
     */
    private fun validateAndAddBarang() {
        val nama = binding.edtNamaBarang.text.toString().trim()
        val harga = binding.edtHarga.text.toString().toIntOrNull()
        val merk = binding.edtMerk.text.toString().trim()
        val kategoriName = binding.edtKategori.text.toString().trim()
        val deskripsi = binding.edtDeskripsi.text.toString().trim()

        if (nama.isBlank() || harga == null || harga <= 0 || merk.isBlank() ||
            kategoriName.isBlank() || deskripsi.isBlank()) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        val kategoriId = kategoriMap[kategoriName]
        if (kategoriId == null) {
            Toast.makeText(this, "Invalid category selected", Toast.LENGTH_SHORT).show()
            return
        }

        val newBarang = Barang(
            namabarang = nama,
            harga = harga,
            merk = merk,
            id_kategori = kategoriId,
            deskripsi = deskripsi
        )

        addBarang(newBarang)
    }

    /**
     * Sends a request to add a new item.
     */
    private fun addBarang(barang: Barang) {
        lifecycleScope.launch {
            try {
                val response = barangApiService.addBarang(barang)
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@TambahBarangActivity,
                        "Successfully added new item!",
                        Toast.LENGTH_SHORT
                    ).show()
                    clearInputFields()
                    finish()
                } else {
                    handleError("Failed to add item: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("An error occurred: ${e.message}")
            }
        }
    }

    /**
     * Clears all input fields.
     */
    private fun clearInputFields() {
        binding.edtNamaBarang.setText("")
        binding.edtHarga.setText("")
        binding.edtMerk.setText("")
        binding.edtKategori.setText("")
        binding.edtDeskripsi.setText("")
    }

    /**
     * Displays an error message and logs the error.
     */
    private fun handleError(message: String) {
        Log.e("TambahBarangActivity", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
