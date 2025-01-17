package com.konyol.babycarex.activity.guest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.konyol.babycarex.R
import com.konyol.babycarex.data.network.AuthApiService
import com.konyol.babycarex.data.request.PasswordRequest
import com.konyol.babycarex.databinding.ActivityForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {
    @Inject
    lateinit var authApiService: AuthApiService
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewFlipper: ViewFlipper
    private var passwordRequest: PasswordRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backToMainForgot.setOnClickListener { finish() }
        binding.edtEmailForgot.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.edtEmailForgot.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        viewFlipper = binding.viewFlipper
        viewFlipper.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        viewFlipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)

        binding.LBtnSendOtp.setOnClickListener { submitEmail() }
        binding.LBtnConfirmOtp.setOnClickListener { submitOtp() }
        binding.LBtnConfirmNewPassword.setOnClickListener { submitNewPassword() }
    }

    private fun submitEmail() {
        val email = binding.edtEmailForgot.text.toString()
        if (email.isEmpty()) {
            binding.edtEmailForgot.error = "Email is not valid"
            return
        }

        passwordRequest = PasswordRequest(email = email)
        lifecycleScope.launch {
            try {
                val response = authApiService.forgotPassword(passwordRequest!!)
                if (response.isSuccessful) {
                    viewFlipper.showNext()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(this@ForgotPasswordActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e("Login", "Error: $errorMessage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@ForgotPasswordActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitOtp() {
        val otp = binding.edtOtp.text.toString()

        if (otp.isEmpty()) {
            binding.edtOtp.error = "Otp is not valid"
            return
        }

        passwordRequest?.otp = otp
        viewFlipper.showNext()
    }

    private fun submitNewPassword() {
        val password = binding.edtNewPassword.text.toString()
        val confirmPassword = binding.edtConfirmPassword.text.toString()

        if (password.isEmpty()) {
            binding.edtNewPassword.error = "Password is not valid"
            return
        }
        if (password != confirmPassword) {
            binding.edtNewPassword.error = "Password and password confirmation is not same"
            return
        }
        passwordRequest?.password = password
        passwordRequest?.password_confirmation = confirmPassword

        lifecycleScope.launch {
            try {
                val response = authApiService.updatePassword(passwordRequest!!)
                if (response.isSuccessful) {
                    Toast.makeText(this@ForgotPasswordActivity, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    viewFlipper.showPrevious()
                    Toast.makeText(this@ForgotPasswordActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e("Login", "Error: $errorMessage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@ForgotPasswordActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}