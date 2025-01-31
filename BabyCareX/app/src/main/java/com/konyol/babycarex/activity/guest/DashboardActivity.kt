package com.konyol.babycarex.activity.guest

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.konyol.babycarex.activity.admin.AdminDashboardActivity
import com.konyol.babycarex.databinding.ActivityDashboardBinding
import com.konyol.babycarex.activity.kasir.KasirHomeActivity
import com.konyol.babycarex.data.network.AuthApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    @Inject
    lateinit var authApiService: AuthApiService
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.cardAdmin.setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }
        binding.cardKasir.setOnClickListener {
            val intent = Intent(this, KasirHomeActivity::class.java)
            startActivity(intent)
        }
        binding.cardLogout.setOnClickListener {
            lifecycleScope.launch {
                authApiService.logout()
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}