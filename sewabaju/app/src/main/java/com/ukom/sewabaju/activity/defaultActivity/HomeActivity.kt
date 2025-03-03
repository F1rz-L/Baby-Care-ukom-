package com.ukom.sewabaju.activity.defaultActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ukom.sewabaju.R
import com.ukom.sewabaju.activity.guestActivity.LoginActivity
import com.ukom.sewabaju.databinding.ActivityHomeBinding
import com.ukom.sewabaju.repository.AuthRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.clearCustomData
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.getCustomData

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
    }

    private fun initLayout() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        authRepository = AuthRepository(binding.root)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (getCustomData("role") == "customer") binding.btnAdmin.visibility = View.GONE else binding.btnCustomer.visibility = View.GONE
    }

    private fun handleEvent() {
        binding.btnCustomer.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CustomerActivity::class.java))
        }
        binding.btnAdmin.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AdminActivity::class.java))
        }
        binding.btnLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        authRepository.logout({
            clearSession()
        }, {
            clearSession()
        })
    }

    private fun clearSession() {
        clearCustomData("token")
        clearCustomData("role")

        Intent(this@HomeActivity, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }.let {
            startActivity(it)
        }

        finish()
    }
}