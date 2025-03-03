package com.ukom.sewabaju.activity.guestActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ukom.sewabaju.R
import com.ukom.sewabaju.activity.defaultActivity.HomeActivity
import com.ukom.sewabaju.databinding.ActivityLoginBinding
import com.ukom.sewabaju.model.request.LoginRequest
import com.ukom.sewabaju.repository.AuthRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.setCustomData

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
    }

    private fun initLayout() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        setContentView(binding.root)
        authRepository = AuthRepository(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleEvent() {
        binding.btnLogin.setOnClickListener { login() }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }

    private fun login() {
        if (!validation()) return

        binding.btnLogin.isEnabled = false
        authRepository.login(
            loginRequest = LoginRequest(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            ),
            onSuccess = { data ->
                binding.btnLogin.isEnabled = true
                setCustomData("token", data.token!!)
                setCustomData("role", data.user!!.role)

                Intent(this@LoginActivity, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                }.let {
                    startActivity(it)
                }
                
                finish()
            },
            onFailure = { binding.btnLogin.isEnabled = true }
        )
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.etEmail.text.toString().trim().isEmpty()) {
            binding.etEmail.error = "Email harus diisi"
            isValid = false
        }

        if (binding.etPassword.text.toString().trim().isEmpty()) {
            binding.etPassword.error = "Password harus diisi"
            isValid = false
        }

        return isValid
    }
}