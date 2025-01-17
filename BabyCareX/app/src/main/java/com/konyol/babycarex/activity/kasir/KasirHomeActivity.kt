// KasirHomeActivity.kt
package com.konyol.babycarex.activity.kasir

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
import com.konyol.babycarex.adapter.BarangKasirAdapter
import com.konyol.babycarex.activity.admin.TambahBarangActivity
import com.konyol.babycarex.adapter.BarangAdminAdapter
import com.konyol.babycarex.data.model.Barang
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.databinding.ActivityKasirHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class KasirHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKasirHomeBinding

    // Inject the adapter
    @Inject
    lateinit var barangApiService: BarangApiService

    lateinit var kasirAdapter: BarangKasirAdapter
    private val barangList = mutableListOf<Barang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityKasirHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnTambahBarang.setOnClickListener {
            startActivity(Intent(this, TambahBarangActivity::class.java))
        }

        // Set up RecyclerView with injected adapter
        kasirAdapter = BarangKasirAdapter(barangList)
        binding.rvBarangs.layoutManager = LinearLayoutManager(this)
        binding.rvBarangs.adapter = kasirAdapter
    }

    private fun setupRecyclerView() {
//        kasirAdapter = BarangAdminAdapter(barangList,
//            onEditClick = { barang ->
//                Toast.makeText(this, "Edit: ${barang.namabarang}", Toast.LENGTH_SHORT).show()
//            },
//            onDeleteClick = { barang ->
//                deleteBarang(barang) // Handle the delete action
//            }
//        )
        binding.rvBarangs.apply {
            layoutManager = LinearLayoutManager(this@KasirHomeActivity)
            adapter = kasirAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        fetchBarang() // Re-fetch data every time the activity resumes
    }

    private fun fetchBarang() {
        lifecycleScope.launch {
            try {
                val response = barangApiService.getAllBarang()
                if (response.isSuccessful && response.body() != null) {
                    val barangResponse = response.body()!!
                    val barangList = barangResponse.data
                    this@KasirHomeActivity.barangList.clear()
                    this@KasirHomeActivity.barangList.addAll(barangList)
                    kasirAdapter.notifyDataSetChanged()
                } else {
                    Log.e("FetchBarang", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@KasirHomeActivity,
                        "Failed to fetch barang",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("FetchBarang", "Exception: ${e.message}")
                Toast.makeText(this@KasirHomeActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
