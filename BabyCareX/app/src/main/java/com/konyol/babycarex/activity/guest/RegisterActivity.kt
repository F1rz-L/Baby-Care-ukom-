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
import com.konyol.babycarex.data.network.AuthApiService
import com.konyol.babycarex.data.request.RegisterRequest
import com.konyol.babycarex.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    @Inject
    lateinit var authApiService: AuthApiService
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            val nama = binding.edtFullname.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val alamat = binding.edtAlamat.text.toString()
            val nomor_telepon = binding.edtNomorTelepon.text.toString()
            val nik = binding.edtNik.text.toString()

            register(nama, email, password, alamat, nomor_telepon, nik)
        }
        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register(
        nama: String,
        email: String,
        password: String,
        alamat: String,
        nomor_telepon: String,
        nik: String
    ) {
        Toast.makeText(this@RegisterActivity, "Registering..", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            try {

                val response = authApiService.register(
                    RegisterRequest(
                        nama,
                        email,
                        password,
                        alamat,
                        nomor_telepon,
                        nik
                    )
                )
                if (response.isSuccessful) {
                    LocalDataManager(this@RegisterActivity).setToken(response.body()!!.data.token)
                    Log.d("Login", "Token: " + response.body()!!.data.token)

                    authApiService.requestOTP(email)

                    val intent = Intent(this@RegisterActivity, VerificationActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Login", "Error: $errorMessage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@RegisterActivity, "An error occurred: $e", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}