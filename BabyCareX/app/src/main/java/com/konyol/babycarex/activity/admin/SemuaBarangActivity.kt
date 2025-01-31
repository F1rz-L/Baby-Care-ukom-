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
import com.konyol.babycarex.adapter.BarangAdminAdapter
import com.konyol.babycarex.databinding.ActivitySemuaBarangBinding
import com.konyol.babycarex.data.model.Barang
import com.konyol.babycarex.data.network.BarangApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SemuaBarangActivity : AppCompatActivity() {

    @Inject
    lateinit var barangApiService: BarangApiService
    lateinit var binding: ActivitySemuaBarangBinding
    private lateinit var barangAdapter: BarangAdminAdapter
    private val barangList = mutableListOf<Barang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySemuaBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchBarang()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnTambahBarang.setOnClickListener {
            startActivity(Intent(this, TambahBarangActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        fetchBarang() // Re-fetch data every time the activity resumes
    }

    private fun setupRecyclerView() {
        barangAdapter = BarangAdminAdapter(barangList,
            onEditClick = { barang ->
                Toast.makeText(this, "Edit: ${barang.namabarang}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { barang ->
                deleteBarang(barang) // Handle the delete action
            }
        )
        binding.rvSemuaBarang.apply {
            layoutManager = LinearLayoutManager(this@SemuaBarangActivity)
            adapter = barangAdapter
        }
    }

    private fun fetchBarang() {
        lifecycleScope.launch {
            try {
                val response = barangApiService.getAllBarang()
                if (response.isSuccessful && response.body() != null) {
                    val barangResponse = response.body()!!
                    val barangList = barangResponse.data
                    this@SemuaBarangActivity.barangList.clear()
                    this@SemuaBarangActivity.barangList.addAll(barangList)
                    barangAdapter.notifyDataSetChanged()
                } else {
                    Log.e("FetchBarang", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@SemuaBarangActivity,
                        "Failed to fetch categories",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("FetchBarang", "Exception: ${e.message}")
                Toast.makeText(this@SemuaBarangActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun deleteBarang(barang: Barang) {
        lifecycleScope.launch {
            try {
                val response = barangApiService.deleteBarang(barang.id!!)
                if (response.isSuccessful) {
                    // Delete from the adapter's list directly
                    val position = barangList.indexOf(barang)
                    barangList.removeAt(position)
                    barangAdapter.notifyItemRemoved(position)

                    Toast.makeText(
                        this@SemuaBarangActivity,
                        "Barang deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e("DeleteBarang", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@SemuaBarangActivity,
                        "Failed to delete barang",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("DeleteBarang", "Exception: ${e.message}")
                Toast.makeText(this@SemuaBarangActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}
