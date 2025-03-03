package com.konyol.babycarex.activity.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.konyol.babycarex.R
import com.konyol.babycarex.databinding.ActivityTambahPelangganBinding
import com.konyol.babycarex.data.model.Pelanggan
import com.konyol.babycarex.data.network.PelangganApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TambahPelangganActivity : AppCompatActivity() {
    @Inject
    lateinit var pelangganApiService: PelangganApiService
    private lateinit var binding: ActivityTambahPelangganBinding

    private val pelangganId by lazy { intent.getIntExtra("pelangganId", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTambahPelangganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (pelangganId != 0) {
            lifecycleScope.launch {
                try {
                    val response = pelangganApiService.getPelangganById(pelangganId)
                    if (response.isSuccessful) {
                        val pelanggan = response.body()!!.data
                        binding.etNamaPelanggan.setText(pelanggan.nama)
                        binding.etAlamatPelanggan.setText(pelanggan.alamat)
                        binding.etNoTelp.setText(pelanggan.nomor_telepon)
                        binding.etNIK.setText(pelanggan.nik)
                        binding.btnTmbhPelanggan.text = "Edit Pelanggan"
                        binding.tvDisplayPelanggan.text = "Edit Pelanggan"
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Failed to fetch pelanggan: ${e.message}")
                }
            }
        }

        // Create a new "pelanggan"
        binding.btnTmbhPelanggan.setOnClickListener {
            val namaText = binding.etNamaPelanggan.text.toString()
            val alamatText = binding.etAlamatPelanggan.text.toString()
            val noTelpText = binding.etNoTelp.text.toString()
            val nikText = binding.etNIK.text.toString()

            if (namaText.isNotBlank() && alamatText.isNotBlank() && noTelpText.isNotBlank() && nikText.isNotBlank()) {
                val pelangganData = Pelanggan(
                    nama = namaText,
                    alamat = alamatText,
                    nomor_telepon = noTelpText,
                    nik = nikText
                )
                if (pelangganId != 0) {
                    updatePelanggan(pelangganData)
                } else {
                    addPelanggan(pelangganData)
                }
            } else {
                Toast.makeText(this, "Pelanggan cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addPelanggan(pelanggan: Pelanggan) {
        lifecycleScope.launch {
            try {
                val response = pelangganApiService.addPelanggan(pelanggan)
                if (response.isSuccessful) {
                    val addedPelanggan = response.body()
                    Log.d("TAG", "Pelanggan added: $addedPelanggan")
                    // Handle the success, e.g., update UI or notify user
                    Toast.makeText(
                        this@TambahPelangganActivity,
                        "Berhasil menambahkan pelanggan baru!",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.etNamaPelanggan.setText("")
                    binding.etAlamatPelanggan.setText("")
                    binding.etNoTelp.setText("")
                    binding.etNIK.setText("")
                    finish()
                } else {
                    // Handle the error
                    Toast.makeText(
                        this@TambahPelangganActivity,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@TambahPelangganActivity,
                    "Exception: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updatePelanggan(pelanggan: Pelanggan) {
        lifecycleScope.launch {
            try {
                val response = pelangganApiService.updatePelanggan(pelangganId, pelanggan)
                if (response.isSuccessful) {
                    val updatedPelanggan = response.body()
                    Log.d("TAG", "Pelanggan updated: $updatedPelanggan")
                    // Handle the success, e.g., update UI or notify user
                    Toast.makeText(
                        this@TambahPelangganActivity,
                        "Berhasil memperbarui pelanggan!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    // Handle the error
                    Toast.makeText(
                        this@TambahPelangganActivity,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@TambahPelangganActivity,
                    "Exception: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}