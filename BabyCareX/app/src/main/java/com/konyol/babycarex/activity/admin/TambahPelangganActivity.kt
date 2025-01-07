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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTambahPelangganBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Create a new "pelanggan"
        binding.btnTmbhPelanggan.setOnClickListener {
            val namaText = binding.etNamaPelanggan.text.toString()
            val alamatText = binding.etAlamatPelanggan.text.toString()
            val noTelpText = binding.etNoTelp.text.toString()
            val nikText = binding.etNIK.text.toString()

            if (namaText.isNotBlank() && alamatText.isNotBlank() && noTelpText.isNotBlank() && nikText.isNotBlank()) {
                val newPelanggan = Pelanggan(
                    nama = namaText,
                    alamat = alamatText,
                    nomor_telepon = noTelpText,
                    nik = nikText
                )
                addPelanggan(newPelanggan)
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
        // Use a coroutine scope for the network request
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
}