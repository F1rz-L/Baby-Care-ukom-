package com.konyol.babycarex.activity.guest

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
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
import com.konyol.babycarex.databinding.ActivityVerificationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VerificationActivity : AppCompatActivity() {
    @Inject
    lateinit var authApiService: AuthApiService
    private lateinit var binding: ActivityVerificationBinding
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startCountdown()
        binding.VBtn.setOnClickListener {
            val otp = binding.otpVerification.text.toString()
            val email = intent.getStringExtra("email")
            if (email != null) {
                verify(otp, email)
            }
        }
    }

    private fun startCountdown() {
        binding.txtKirimUlang.isEnabled = false
        countDownTimer = object : CountDownTimer(300000, 1000) { // 5 minutes in milliseconds
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60
                var minutesText = if (minutes < 10) "0$minutes" else minutes.toString()
                var secondsText = if (seconds < 10) "0$seconds" else seconds.toString()
                binding.txtKirimUlang.text = "Kirim ulang dalam $minutesText:$secondsText"
                binding.txtKirimUlang.isEnabled = false
                binding.txtKirimUlang.paintFlags = 0
            }

            override fun onFinish() {
                binding.txtKirimUlang.isEnabled = true
                binding.txtKirimUlang.text = "Kirim ulang"
                binding.txtKirimUlang.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
        }.start()
    }

    private fun verify(otp: String, email: String) {
        lifecycleScope.launch {
            try {
                val response = authApiService.verifyOTP(email, otp)
                if (response.isSuccessful) {
                    Toast.makeText(this@VerificationActivity, "Verification successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@VerificationActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(this@VerificationActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e("Login", "Error: $errorMessage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@VerificationActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}