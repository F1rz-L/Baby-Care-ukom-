package com.konyol.babycarex.activity.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.konyol.babycarex.R
import com.konyol.babycarex.databinding.ActivityNotaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotaBinding
    private val pinjamanId by lazy { intent.getIntExtra("pinjamanId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotaBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handleEvent()
        loadData()
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnKirimKeWhatsapp.setOnClickListener {
//            NotaImageBuilder(order, kostumRepository).generateAndShareReceipt(this@NotaActivity)
        }
    }

    private fun loadData() {

    }
}