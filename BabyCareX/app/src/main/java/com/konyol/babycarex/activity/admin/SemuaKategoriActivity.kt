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
import androidx.recyclerview.widget.LinearLayoutManager
import com.konyol.babycarex.adapter.KategoriAdapter
import com.konyol.babycarex.databinding.ActivitySemuaKategoriBinding
import com.konyol.babycarex.data.model.Kategori
import com.konyol.babycarex.data.network.KategoriApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SemuaKategoriActivity : AppCompatActivity() {

    @Inject
    lateinit var kategoriApiService: KategoriApiService
    lateinit var binding: ActivitySemuaKategoriBinding
    private lateinit var kategoriAdapter: KategoriAdapter
    private val kategoriList = mutableListOf<Kategori>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySemuaKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchKategori()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnTambahKategori.setOnClickListener {
            startActivity(Intent(this, TambahKategoriActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        fetchKategori() // Re-fetch data every time the activity resumes
    }

    private fun setupRecyclerView() {
        kategoriAdapter = KategoriAdapter(kategoriList,
            onEditClick = { kategori ->
                Toast.makeText(this, "Edit: ${kategori.kategori}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { kategori ->
                deleteKategori(kategori) // Handle the delete action
            }
        )
        binding.rvSemuaKategori.apply {
            layoutManager = LinearLayoutManager(this@SemuaKategoriActivity)
            adapter = kategoriAdapter
        }
    }

    private fun fetchKategori() {
        lifecycleScope.launch {
            try {
                val response = kategoriApiService.getAllKategori()
                if (response.isSuccessful && response.body() != null) {
                    val kategoriResponse = response.body()!!
                    val kategoriList = kategoriResponse.data
                    this@SemuaKategoriActivity.kategoriList.clear()
                    this@SemuaKategoriActivity.kategoriList.addAll(kategoriList)
                    kategoriAdapter.notifyDataSetChanged()
                } else {
                    Log.e("FetchKategori", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@SemuaKategoriActivity,
                        "Failed to fetch categories",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("FetchKategori", "Exception: ${e.message}")
                Toast.makeText(this@SemuaKategoriActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun deleteKategori(kategori: Kategori) {
        lifecycleScope.launch {
            try {
                val response = kategoriApiService.deleteKategori(kategori.id!!)
                if (response.isSuccessful) {
                    // Delete from the adapter's list directly
                    val position = kategoriList.indexOf(kategori)
                    kategoriList.removeAt(position)
                    kategoriAdapter.notifyItemRemoved(position)

                    Toast.makeText(
                        this@SemuaKategoriActivity,
                        "Kategori deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e("DeleteKategori", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@SemuaKategoriActivity,
                        "Failed to delete kategori",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("DeleteKategori", "Exception: ${e.message}")
                Toast.makeText(this@SemuaKategoriActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}
