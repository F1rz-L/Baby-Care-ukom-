package com.ukom.sewabaju.activity.defaultActivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ukom.sewabaju.R
import com.ukom.sewabaju.databinding.ActivityProfileBinding
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.model.User
import com.ukom.sewabaju.model.request.UserRequest
import com.ukom.sewabaju.repository.AuthRepository
import com.ukom.sewabaju.repository.UserRepository

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    private var oldBitmap: Bitmap? = null
    private var newBitmap: Bitmap? = null

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val IMAGE_PICK_CODE = 1
    private val CAMERA_CAPTURE_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
        loadData()
    }

    private fun initLayout() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        authRepository = AuthRepository(binding.root)
        userRepository = UserRepository(binding.root)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { saveData() }
        binding.ivOpenCamera.setOnClickListener { checkCameraPermission() }
        binding.ivOpenGallery.setOnClickListener { openGallery() }
    }

    private fun saveData() {
        if (!validation()) return

        binding.btnSave.isEnabled = false
        userRepository.updateUser(
            UserRequest(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etAddress.text.toString(),
                binding.etPhoneNumber.text.toString()
            ),
            {
                binding.btnSave.isEnabled = true
                if (newBitmap != null) {
                    userRepository.uploadImage(newBitmap!!) {
                        MessageHelper.successResult(binding.root, "Sukses menyimpan perubahan")
                    }
                } else {
                    MessageHelper.successResult(binding.root, "Sukses menyimpan perubahan")
                }
            }, { binding.btnSave.isEnabled = true }
        )
    }

    private fun loadData() {
        authRepository.me { user ->
            binding.etName.setText(user.name)
            binding.etEmail.setText(user.email)
            binding.etAddress.setText(user.address)
            binding.etPhoneNumber.setText(user.phoneNumber)

            userRepository.getUserImage(user.image) { bitmap ->
                oldBitmap = bitmap
                binding.image = BitmapDrawable(binding.root.resources, bitmap)
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.etName.text.trim().toString().isEmpty()) {
            binding.etName.error = "Nama harus diisi"
            isValid = false
        }

        if (binding.etEmail.text.trim().toString().isEmpty()) {
            binding.etEmail.error = "Email harus diisi"
            isValid = false
        }

        if (binding.etAddress.text.trim().toString().isEmpty()) {
            binding.etAddress.error = "Alamat harus diisi"
            isValid = false
        }

        if (binding.etPhoneNumber.text.trim().toString().isEmpty()) {
            binding.etPhoneNumber.error = "Nomor Telepon harus diisi"
            isValid = false
        }

        return isValid
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this@ProfileActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                MessageHelper.errorResult(binding.root, "Izin kamera diperlukan")
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_CAPTURE_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_CAPTURE_CODE -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        newBitmap = bitmap
                        binding.image = BitmapDrawable(binding.root.resources, bitmap)
                    }
                }
                IMAGE_PICK_CODE -> {
                    data?.data?.let { uri ->
                        val inputStream = this@ProfileActivity.contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        newBitmap = bitmap
                        binding.image = BitmapDrawable(binding.root.resources, bitmap)
                    }
                }
            }
        }
    }
}