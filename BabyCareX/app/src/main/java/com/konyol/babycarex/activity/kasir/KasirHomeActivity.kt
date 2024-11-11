// KasirHomeActivity.kt
package com.konyol.babycarex.activity.kasir

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.konyol.babycarex.adapter.BarangKasirAdapter
import com.konyol.babycarex.activity.admin.TambahBarangActivity
import com.konyol.babycarex.databinding.ActivityKasirHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KasirHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKasirHomeBinding

    // Inject the adapter
    @Inject lateinit var adapter: BarangKasirAdapter

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
        binding.rvBarangs.layoutManager = LinearLayoutManager(this)
        binding.rvBarangs.adapter = adapter
    }
}
