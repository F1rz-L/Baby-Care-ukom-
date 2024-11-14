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
import com.konyol.babycarex.databinding.ActivityTambahKategoriBinding
import com.konyol.babycarex.data.model.Kategori
import com.konyol.babycarex.data.network.KategoriApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TambahKategoriActivity : AppCompatActivity() {

    @Inject
    lateinit var kategoriApiService: KategoriApiService
    private lateinit var binding: ActivityTambahKategoriBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTambahKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Create a new "kategori"
        binding.btnTmbhKategori.setOnClickListener {
            val kategoriText = binding.etKategori.text.toString()
            if (kategoriText.isNotBlank()) {
                val newKategori = Kategori(kategori = kategoriText)
                addKategori(newKategori)
            } else {
                Toast.makeText(this, "Kategori cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addKategori(kategori: Kategori) {
        // Use a coroutine scope for the network request
        lifecycleScope.launch {
            try {
                val response = kategoriApiService.addKategori(kategori)
                if (response.isSuccessful) {
                    val addedKategori = response.body()
                    Log.d("TAG", "Kategori added: $addedKategori")
                    // Handle the success, e.g., update UI or notify user
                    Toast.makeText(
                        this@TambahKategoriActivity,
                        "Berhasil menambahkan kategori baru!",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.etKategori.setText("")
                    finish()
//                    startActivity(Intent(this@TambahKategoriActivity, AdminDashboardActivity::class.java))
                } else {
                    // Handle the error
                    Toast.makeText(
                        this@TambahKategoriActivity,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@TambahKategoriActivity,
                    "Exception: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}