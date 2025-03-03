package com.ukom.sewabaju.activity.guestActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ukom.sewabaju.R
import com.ukom.sewabaju.activity.defaultActivity.HomeActivity
import com.ukom.sewabaju.databinding.ActivityRegisterBinding
import com.ukom.sewabaju.model.request.RegisterRequest
import com.ukom.sewabaju.repository.AuthRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.setCustomData

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
    }

    private fun initLayout() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        authRepository = AuthRepository(binding.root)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleEvent() {
        binding.btnRegister.setOnClickListener { register() }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun register() {
        if (!validation()) return

        binding.btnRegister.isEnabled = false
        authRepository.register(
            registerRequest = RegisterRequest(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPhoneNumber.text.toString(),
                binding.etAddress.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPasswordConfirmation.text.toString()
            ),
            onSuccess = { data ->
                binding.btnRegister.isEnabled = true
                setCustomData("token", data.token!!)
                setCustomData("role", data.user!!.role)

                Intent(this@RegisterActivity, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                }.let {
                    startActivity(it)
                }

                finish()
            }, { binding.btnRegister.isEnabled = true }
        )
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.etName.error = "Nama harus diisi"
            isValid = false
        }

        if (binding.etEmail.text.toString().trim().isEmpty()) {
            binding.etEmail.error = "Email harus diisi"
            isValid = false
        }

        if (binding.etPhoneNumber.text.toString().trim().isEmpty()) {
            binding.etPhoneNumber.error = "Nomor telepon harus diisi"
            isValid = false
        }

        if (binding.etAddress.text.toString().trim().isEmpty()) {
            binding.etAddress.error = "Alamat harus diisi"
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

        return isValid
    }
}