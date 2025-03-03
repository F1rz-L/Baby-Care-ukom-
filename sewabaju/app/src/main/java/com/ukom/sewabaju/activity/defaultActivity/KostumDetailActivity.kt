package com.ukom.sewabaju.activity.defaultActivity

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ukom.sewabaju.R
import com.ukom.sewabaju.activity.defaultActivity.tambahActivity.TambahKostumActivity
import com.ukom.sewabaju.databinding.ActivityKostumDetailBinding
import com.ukom.sewabaju.dialog.TambahCartDialog
import com.ukom.sewabaju.repository.KostumRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.getCustomData
import org.json.JSONObject

class KostumDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKostumDetailBinding
    private lateinit var kostumRepository: KostumRepository
    private val kostumId by lazy { intent.getIntExtra("kostumId", -1) }
    private lateinit var tambahCartDialog: TambahCartDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
        loadData()
    }

    private fun initLayout() {
        binding = ActivityKostumDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        kostumRepository = KostumRepository(binding.root)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (getCustomData("role") == "admin") {
            binding.ivEdit.visibility = View.VISIBLE
        } else {
            binding.ivEdit.visibility = View.GONE
        }
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener {
            if (getCustomData("role") == "admin") navigateToAdmin() else finish()
        }
        binding.ivEdit.setOnClickListener {
            Intent(this@KostumDetailActivity, TambahKostumActivity::class.java).apply {
                putExtra("kostumId", kostumId)
            }.let {
                startActivity(it)
            }
        }
        binding.btnAddToCart.setOnClickListener {
            tambahCartDialog.show()
        }
    }

    private fun loadData() {
        kostumRepository.getKostumDetail(kostumId) { kostum ->
            binding.name = kostum.name
            binding.description = kostum.description

            tambahCartDialog = TambahCartDialog(binding.root, kostum, kostumRepository)

            kostumRepository.getKostumImage(kostum.image) { bitmap ->
                binding.image = BitmapDrawable(binding.root.resources, bitmap)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (getCustomData("role") == "admin") navigateToAdmin()
    }

    private fun navigateToAdmin() {
        Intent(this@KostumDetailActivity, AdminActivity::class.java).apply {
            putExtra("FRAGMENT_INDEX", 1)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }.let {
            startActivity(it)
        }
        finish()
    }
}