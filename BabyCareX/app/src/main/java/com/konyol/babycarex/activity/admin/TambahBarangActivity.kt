package com.konyol.babycarex.activity.admin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.konyol.babycarex.R
import com.konyol.babycarex.data.MessageHelper
import com.konyol.babycarex.data.di.NetworkModule
import com.konyol.babycarex.data.model.Barang
import com.konyol.babycarex.data.model.Pelanggan
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.data.network.KategoriApiService
import com.konyol.babycarex.data.network.PelangganApiService
import com.konyol.babycarex.data.response.APIResponse
import com.konyol.babycarex.databinding.ActivityTambahBarangBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class TambahBarangActivity : AppCompatActivity() {
    @Inject
    lateinit var barangApiService: BarangApiService

    @Inject
    lateinit var kategoriApiService: KategoriApiService

    private val kategoriMap = mutableMapOf<String, Int>() // Maps category names to IDs
    private lateinit var binding: ActivityTambahBarangBinding
    private val barangId by lazy { intent.getIntExtra("barangId", 0) }

    private var oldBitmap: Bitmap? = null
    private var newBitmap: Bitmap? = null

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val IMAGE_PICK_CODE = 1
    private val CAMERA_CAPTURE_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (barangId != 0) {
            lifecycleScope.launch {
                try {
                    val response = barangApiService.getBarangById(barangId)
                    if (response.isSuccessful) {
                        val barang = response.body()!!.data
                        binding.edtNamaBarang.setText(barang.namabarang)
                        binding.edtMerk.setText(barang.merk)
//                        binding.edtKategori.setText(barang.id_kategori.toString())
                        binding.edtHarga.setText(barang.harga.toString())
                        binding.edtDeskripsi.setText(barang.deskripsi)
                        binding.btnTmbhBarang.text = "Edit Barang"
                        binding.tvDisplay.text = "Edit Barang"

                        if (barang.gambar !== null) {
                            val imgUrl = NetworkModule.BASE_IMG_URL + barang?.gambar
                            Picasso.get().load(imgUrl).into(binding.barangPreview)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Failed to fetch barang: ${e.message}")
                }
            }
        }

        // Fetch and populate dropdown
        fetchKategoriList()
        binding.btnTmbhBarang.setOnClickListener {
            validateAndAddBarang()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.takeImage.setOnClickListener {
            checkCameraPermission()
        }
        binding.uploadImage.setOnClickListener {
            openGallery()
        }
    }

    /**
     * Fetches the category list from the API and populates the dropdown.
     */
    private fun fetchKategoriList() {
        lifecycleScope.launch {
            try {
                val response = kategoriApiService.getAllKategori()
                if (response.isSuccessful && response.body() != null) {
                    val kategoriList = response.body()!!.data
                    kategoriList.forEach { kategori ->
                        kategoriMap[kategori.kategori] = kategori.id!!.toInt()
                    }
                    val kategoriNames = kategoriMap.keys.toList()
                    val adapter = ArrayAdapter(
                        this@TambahBarangActivity,
                        R.layout.dropdown_item,
                        kategoriNames
                    )
                    adapter.setDropDownViewResource(R.layout.dropdown_item)
                    binding.edtKategori.setAdapter(adapter)
                } else {
                    handleError("Failed to fetch categories: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("An error occurred while fetching categories: ${e.message}")
            }
        }
    }

    /**
     * Validates the input fields and initiates adding a new item.
     */
    private fun validateAndAddBarang() {
        val nama = binding.edtNamaBarang.text.toString().trim()
        val harga = binding.edtHarga.text.toString().toIntOrNull()
        val merk = binding.edtMerk.text.toString().trim()
        val kategoriName = binding.edtKategori.text.toString().trim()
        val deskripsi = binding.edtDeskripsi.text.toString().trim()

        if (nama.isBlank() || harga == null || harga <= 0 || merk.isBlank() || kategoriName.isBlank() || deskripsi.isBlank()) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        val kategoriId = kategoriMap[kategoriName]
        if (kategoriId == null) {
            Toast.makeText(this, "Invalid category selected", Toast.LENGTH_SHORT).show()
            return
        }

        val newBarang = Barang(
            namabarang = nama,
            harga = harga,
            merk = merk,
            id_kategori = kategoriId,
            deskripsi = deskripsi,
        )

        if (barangId != 0) {
            updateBarang(newBarang, barangId)
        } else {
            addBarang(newBarang)
        }
    }

    /**
     * Sends a request to add a new item.
     */
    private fun addBarang(barang: Barang) {
        lifecycleScope.launch {
            try {
                val response = barangApiService.addBarang(barang)
                if (response.isSuccessful) {
                    Toast.makeText(this@TambahBarangActivity,"Successfully added new item!",Toast.LENGTH_SHORT).show()
                    clearInputFields()
                    if ( newBitmap !== null ) {
                        uploadImage(response.body()?.data?.id!!, newBitmap!!) { finish() }
                    } else {
                        finish()
                    }
                } else {
                    handleError("Failed to add item: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("An error occurred: ${e.message}")
            }
        }
    }
    private fun updateBarang(barang: Barang, barangId: Int) {
        lifecycleScope.launch {
            try {
                val response = barangApiService.updateBarang(barangId, barang)
                if (response.isSuccessful) {
                    Toast.makeText(this@TambahBarangActivity,"Successfully added new item!",Toast.LENGTH_SHORT).show()
                    clearInputFields()
                    if ( newBitmap !== null) {
                        uploadImage(response.body()?.data?.id!!, newBitmap!!) { finish() }
                    } else {
                        finish()
                    }
                } else {
                    handleError("Failed to add item: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError("An error occurred: ${e.message}")
            }
        }
    }

    /**
     * Clears all input fields.
     */
    private fun clearInputFields() {
        binding.edtNamaBarang.setText("")
        binding.edtHarga.setText("")
        binding.edtMerk.setText("")
        binding.edtKategori.setText("")
        binding.edtDeskripsi.setText("")
    }

    /**
     * Displays an error message and logs the error.
     */
    private fun handleError(message: String) {
        Log.e("TambahBarangActivity", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this@TambahBarangActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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

    private fun uploadImage(barangId: Int, bitmap: Bitmap, onSuccess: (Barang) -> Unit) {
        lifecycleScope.launch {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            val requestBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody =
                MultipartBody.Part.createFormData("gambar", "image.jpg", requestBody)

            val response = barangApiService.uploadBarangImage(
                id = barangId,
                image = multipartBody
            )
            if (response.isSuccessful) {
                response.body()?.let { onSuccess(it.data) }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_CAPTURE_CODE -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        newBitmap = bitmap
                        binding.barangPreview.setImageDrawable(
                            BitmapDrawable(
                                binding.root.resources,
                                bitmap
                            )
                        )
                    }
                }

                IMAGE_PICK_CODE -> {
                    data?.data?.let { uri ->
                        val inputStream =
                            this@TambahBarangActivity.contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        newBitmap = bitmap
                        binding.barangPreview.setImageDrawable(
                            BitmapDrawable(
                                binding.root.resources,
                                bitmap
                            )
                        )
                    }
                }
            }
        }
    }
}
