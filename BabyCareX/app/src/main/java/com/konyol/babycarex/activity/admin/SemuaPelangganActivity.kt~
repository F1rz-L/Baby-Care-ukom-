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
import com.konyol.babycarex.adapter.PelangganAdapter
import com.konyol.babycarex.databinding.ActivitySemuaPelangganBinding
import com.konyol.babycarex.data.model.Pelanggan
import com.konyol.babycarex.data.network.PelangganApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SemuaPelangganActivity : AppCompatActivity() {

    @Inject
    lateinit var pelangganApiService: PelangganApiService
    lateinit var binding: ActivitySemuaPelangganBinding
    private lateinit var pelangganAdapter: PelangganAdapter
    private val pelangganList = mutableListOf<Pelanggan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySemuaPelangganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchPelanggan()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnTambahPelanggan.setOnClickListener {
            startActivity(Intent(this, TambahPelangganActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        fetchPelanggan() // Re-fetch data every time the activity resumes
    }

    private fun setupRecyclerView() {
        pelangganAdapter = PelangganAdapter(pelangganList,
            onEditClick = { pelanggan ->
                Toast.makeText(this, "Edit: ${pelanggan.nama}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { pelanggan ->
                deletePelanggan(pelanggan) // Handle the delete action
            }
        )
        binding.rvSemuaPelanggan.apply {
            layoutManager = LinearLayoutManager(this@SemuaPelangganActivity)
            adapter = pelangganAdapter
        }
    }

    private fun fetchPelanggan() {
        lifecycleScope.launch {
            try {
                val response = pelangganApiService.getAllPelanggan()
                if (response.isSuccessful && response.body() != null) {
                    val pelangganResponse = response.body()!!
                    val pelangganList = pelangganResponse.data
                    this@SemuaPelangganActivity.pelangganList.clear()
                    this@SemuaPelangganActivity.pelangganList.addAll(pelangganList)
                    pelangganAdapter.notifyDataSetChanged()

                    // Show or hide the empty list image
                    toggleEmptyListImage()
                } else {
                    Log.e("FetchPelanggan", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@SemuaPelangganActivity,
                        "Failed to fetch categories",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("FetchPelanggan", "Exception: ${e.message}")
                Toast.makeText(this@SemuaPelangganActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun deletePelanggan(pelanggan: Pelanggan) {
        lifecycleScope.launch {
            try {
                val response = pelangganApiService.deletePelanggan(pelanggan.id!!)
                if (response.isSuccessful) {
                    // Delete from the adapter's list directly
                    val position = pelangganList.indexOf(pelanggan)
                    pelangganList.removeAt(position)
                    pelangganAdapter.notifyItemRemoved(position)

                    // Show or hide the empty list image
                    toggleEmptyListImage()

                    Toast.makeText(
                        this@SemuaPelangganActivity,
                        "Pelanggan deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e("DeletePelanggan", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@SemuaPelangganActivity,
                        "Failed to delete pelanggan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("DeletePelanggan", "Exception: ${e.message}")
                Toast.makeText(this@SemuaPelangganActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun toggleEmptyListImage() {
        if (pelangganList.isEmpty()) {
            binding.imgEmptyList.visibility = android.view.View.VISIBLE
        } else {
            binding.imgEmptyList.visibility = android.view.View.GONE
        }
    }
}
