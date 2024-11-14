package com.konyol.babycarex.activity.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.konyol.babycarex.activity.guest.DashboardActivity
import com.konyol.babycarex.R
import com.konyol.babycarex.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {
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
        binding.back.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
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
//        binding.btnTambahPelanggan.setOnClickListener {
//            startActivity(Intent(this, ::class.java))
//        }
    }
}