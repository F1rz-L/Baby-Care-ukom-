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
import com.konyol.babycarex.R
import com.konyol.babycarex.adapter.PelangganAdapter
import com.konyol.babycarex.adapter.PinjamanAdapter
import com.konyol.babycarex.data.model.Pinjaman
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.data.network.PelangganApiService
import com.konyol.babycarex.data.network.PinjamanApiService
import com.konyol.babycarex.databinding.ActivitySemuaPinjamanBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SemuaPinjamanActivity : AppCompatActivity() {
    @Inject
    lateinit var pinjamanApiService: PinjamanApiService

    @Inject
    lateinit var barangApiService: BarangApiService

    @Inject
    lateinit var pelangganApiService: PelangganApiService
    private val pinjamanList = mutableListOf<Pinjaman>()

    private lateinit var pinjamanAdapter: PinjamanAdapter
    private lateinit var binding: ActivitySemuaPinjamanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySemuaPinjamanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchPinjaman()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.back2.setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        pinjamanAdapter = PinjamanAdapter(
            pinjamanList, barangApiService, pelangganApiService, lifecycleOwner = this,
            onReturnClick = { pinjamanId -> kembali(pinjamanId) },
            onStrukClick = { pinjamanId ->
                Intent(this, StrukActivity::class.java).apply {
                    putExtra("pinjamanId", pinjamanId)
                }.let { startActivity(it) }
            }
        )
        binding.rvSemuaPinjaman.apply {
            layoutManager = LinearLayoutManager(this@SemuaPinjamanActivity)
            adapter = pinjamanAdapter
        }
    }

    private fun fetchPinjaman() {
        lifecycleScope.launch {
            try {
                val response = pinjamanApiService.getAllPinjaman()
                if (response.isSuccessful && response.body() != null) {
                    val pinjamanResponse = response.body()!!
                    val pinjamanList = pinjamanResponse.data
                    this@SemuaPinjamanActivity.pinjamanList.clear()
                    this@SemuaPinjamanActivity.pinjamanList.addAll(pinjamanList)
                    pinjamanAdapter.notifyDataSetChanged()
                } else {
                    Log.e("FetchPinjaman", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@SemuaPinjamanActivity,
                        "Failed to fetch pinjaman",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("FetchPelanggan", "Exception: ${e.message}")
                Toast.makeText(this@SemuaPinjamanActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun kembali(pinjamanId: Int) {
        lifecycleScope.launch {
            try {
                Log.d("KembaliPinjaman", "Pinjaman ID: $pinjamanId")
                val response = pinjamanApiService.kembali(pinjamanId)
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@SemuaPinjamanActivity,
                        "Kembalikan pinjaman berhasil!",
                        Toast.LENGTH_SHORT
                    ).show()
                    fetchPinjaman()
                } else {
                    Log.e("KembaliPinjaman", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@SemuaPinjamanActivity,
                        "Failed to return pinjaman",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("KembaliPinjaman", "Exception: ${e.message}")
                Toast.makeText(this@SemuaPinjamanActivity, "An error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}