package com.konyol.babycarex.activity.guest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.konyol.babycarex.R
import com.konyol.babycarex.config.LocalDataManager
import com.konyol.babycarex.data.model.Pelanggan
import com.konyol.babycarex.data.network.AuthApiService
import com.konyol.babycarex.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var authApiService: AuthApiService
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.LBtn1.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            login(email, password)
        }
        binding.toForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(email: String, password: String) {
        Toast.makeText(this@LoginActivity, "Logging in..", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            try {
                val response = authApiService.login(email, password)
                if (response.isSuccessful) {
                    LocalDataManager(this@LoginActivity).setToken(response.body()!!.data.token)
                    Log.d("Login", "Token: " + response.body()!!.data.token)
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(this@LoginActivity, "Error: $errorMessage", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("Login", "Error: $errorMessage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@LoginActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}