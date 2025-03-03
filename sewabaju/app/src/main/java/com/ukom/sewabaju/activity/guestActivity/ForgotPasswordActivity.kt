package com.ukom.sewabaju.activity.guestActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ukom.sewabaju.R
import com.ukom.sewabaju.databinding.ActivityForgotPasswordBinding
import com.ukom.sewabaju.repository.AuthRepository

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var authRepository: AuthRepository
    private var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
    }

    private fun initLayout() {
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        authRepository = AuthRepository(binding.root)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.sendOtpSection.visibility = View.VISIBLE
        binding.updatePasswordSection.visibility = View.GONE
    }

    private fun handleEvent() {
        binding.btnSendOtp.setOnClickListener { sendOtp() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.tvLogin.setOnClickListener {
            Intent(this@ForgotPasswordActivity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }.let {
                startActivity(it)
            }

            finish()
        }
    }

    private fun sendOtp() {
        if (binding.etEmail.text.trim().toString().isEmpty()) {
            binding.etEmail.error = "Email harus diisi"
            return
        }

        email = binding.etEmail.text.toString()
        binding.btnSendOtp.isEnabled = false
        authRepository.forgotPassword(email, {
            binding.btnSendOtp.isEnabled = true

            binding.sendOtpSection.visibility = View.GONE
            binding.updatePasswordSection.visibility = View.VISIBLE
        }, { binding.btnSendOtp.isEnabled = true })
    }

    private fun submit() {
        var isValid = true
        if (binding.etOtp.text.trim().toString().isEmpty()) {
            binding.etOtp.error = "Otp harus diisi"
            isValid = false
        }

        if (binding.etPassword.text.toString().trim().isEmpty()) {
            binding.etPassword.error = "Password harus diisi"
            isValid = false
        }

        if (binding.etPassword.text.toString() != binding.etPasswordConfirmation.text.toString()) {
            binding.etPassword.error = "Password dan konfirmasi password harus sama"
            binding.etPasswordConfirmation.error = "Password dan konfirmasi password harus sama"
            isValid = false
        }

        if (!isValid) return

        binding.btnSubmit.isEnabled = false
        authRepository.updatePassword(
            email,
            binding.etOtp.text.toString(),
            binding.etPassword.text.toString(),
            binding.etPasswordConfirmation.text.toString()
        , {
            binding.btnSendOtp.isEnabled = true
            Intent(this@ForgotPasswordActivity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }.let {
                startActivity(it)
            }

            finish()
        }, { binding.btnSubmit.isEnabled = true })
    }
}