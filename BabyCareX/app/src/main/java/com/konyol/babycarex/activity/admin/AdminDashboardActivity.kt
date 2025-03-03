package com.konyol.babycarex.activity.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.konyol.babycarex.activity.guest.DashboardActivity
import com.konyol.babycarex.R
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.databinding.ActivityAdminDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AdminDashboardActivity : AppCompatActivity() {
    @Inject lateinit var barangApiService: BarangApiService
    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            val barangCount = barangApiService.statsTotalBarang()
            val barangDipinjam = barangApiService.statsBarangDipinjam()
            val totalPinjaman = barangApiService.statsTotalPinjaman()
            binding.tvSemuaBarang.text = barangCount.body().toString()
            binding.tvDipinjam.text = barangDipinjam.body().toString()
            binding.tvTotalPinjaman.text = totalPinjaman.body().toString()
        }

        handleEvent()
    }

    private fun handleEvent() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.btnSemuaPinjaman.setOnClickListener {
            startActivity(Intent(this, SemuaPinjamanActivity::class.java))
        }

        binding.btnSemuaBarang.setOnClickListener {
            startActivity(Intent(this, SemuaBarangActivity::class.java))
        }
        binding.btnTmbhBarang.setOnClickListener {
            startActivity(Intent(this, TambahBarangActivity::class.java))
        }

        binding.btnSemuaKategori.setOnClickListener {
            startActivity(Intent(this, SemuaKategoriActivity::class.java))
        }
        binding.btnTmbhKategori.setOnClickListener {
            startActivity(Intent(this, TambahKategoriActivity::class.java))
        }

        binding.btnSemuaPelanggan.setOnClickListener {
            startActivity(Intent(this, SemuaPelangganActivity::class.java))
        }
        binding.btnTambahPelanggan.setOnClickListener {
            startActivity(Intent(this, TambahPelangganActivity::class.java))
        }
    }
}