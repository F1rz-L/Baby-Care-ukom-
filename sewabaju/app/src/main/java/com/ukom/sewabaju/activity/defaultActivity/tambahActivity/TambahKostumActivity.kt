package com.ukom.sewabaju.activity.defaultActivity.tambahActivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukom.sewabaju.R
import com.ukom.sewabaju.activity.defaultActivity.AdminActivity
import com.ukom.sewabaju.activity.defaultActivity.KostumDetailActivity
import com.ukom.sewabaju.adapter.recyclerView.SizeStockPriceAdapter
import com.ukom.sewabaju.databinding.ActivityTambahKostumBinding
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.model.Category
import com.ukom.sewabaju.model.SizeStockPrice
import com.ukom.sewabaju.model.request.KostumRequest
import com.ukom.sewabaju.repository.CategoryRepository
import com.ukom.sewabaju.repository.KostumRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TambahKostumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahKostumBinding
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var kostumRepository: KostumRepository
    private val kostumId by lazy { intent.getIntExtra("kostumId", -1) }

    private var sizeStockPriceData: MutableList<SizeStockPrice> = mutableListOf()
    private lateinit var sizeStockPriceAdapter: SizeStockPriceAdapter

    private lateinit var categoryList: List<Category>
    private var selectedCategory = ""

    private var oldBitmap: Bitmap? = null
    private var newBitmap: Bitmap? = null

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val IMAGE_PICK_CODE = 1
    private val CAMERA_CAPTURE_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
    }

    private fun initLayout() {
        binding = ActivityTambahKostumBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        categoryRepository = CategoryRepository(binding.root)
        kostumRepository = KostumRepository(binding.root)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (kostumId == -1) {
            binding.tvKeterangan.text = "Tambah Kostum"
            binding.btnTambah.text = "Tambah"
            binding.rvSizeStockPrice.layoutManager = LinearLayoutManager(this@TambahKostumActivity)
            sizeStockPriceAdapter = SizeStockPriceAdapter(sizeStockPriceData, { index, data ->
                syncUnit(index, data)
            }, {
                deleteUnit(it)
            })
            binding.rvSizeStockPrice.adapter = sizeStockPriceAdapter
            loadCategory()
        } else {
            binding.tvKeterangan.text = "Edit Kostum"
            binding.btnTambah.text = "Perbarui"
            loadCategory()
        }
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener { finish() }
        binding.ivDelete.setOnClickListener { deleteData() }
        binding.btnTambahUnit.setOnClickListener { tambahUnit() }
        binding.btnTambah.setOnClickListener { saveData() }
        binding.ivOpenCamera.setOnClickListener { checkCameraPermission() }
        binding.ivOpenGallery.setOnClickListener { openGallery() }
    }

    private fun saveData() {
        if (!validation()) return

        lifecycleScope.launch {
            sizeStockPriceAdapter.updateData(sizeStockPriceData)

            delay(500)

            binding.btnTambah.isEnabled = false
            if (kostumId == -1) {
                kostumRepository.insertKostum(
                    KostumRequest(
                        binding.etName.text.toString(),
                        binding.etDescription.text.toString(),
                        categoryList.find { it.name == selectedCategory }!!.id,
                        sizeStockPriceData
                    ), { kostum ->
                        binding.btnTambah.isEnabled = true
                        kostumRepository.uploadImage(kostum.id, newBitmap!!) { navigateToAdmin() }
                    }, { binding.btnTambah.isEnabled = true }
                )

            } else {
                kostumRepository.updateKostum(
                    kostumId,
                    KostumRequest(
                        binding.etName.text.toString(),
                        binding.etDescription.text.toString(),
                        categoryList.find { it.name == selectedCategory }!!.id,
                        sizeStockPriceData
                    ), {
                        binding.btnTambah.isEnabled = true
                        if (newBitmap != null) {
                            kostumRepository.uploadImage(kostumId, newBitmap!!) {
                                Intent(this@TambahKostumActivity, KostumDetailActivity::class.java).apply {
                                    putExtra("kostumId", kostumId)
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                }.let {
                                    startActivity(it)
                                }
                                finish()
                            }
                        } else {
                            Intent(this@TambahKostumActivity, KostumDetailActivity::class.java).apply {
                                putExtra("kostumId", kostumId)
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            }.let {
                                startActivity(it)
                            }
                            finish()
                        }
                    }, { binding.btnTambah.isEnabled = true }
                )
            }
        }
    }

    private fun deleteData() {
        kostumRepository.deleteKostum(kostumId) { navigateToAdmin() }
    }

    private fun loadCategory() {
        categoryRepository.getCategories { categories ->
            categoryList = categories
            val categoryNames = categories.map { it.name }
            binding.autoCompleteCategory.apply {
                setAdapter(ArrayAdapter(this@TambahKostumActivity, android.R.layout.simple_dropdown_item_1line, categoryNames))
                setOnItemClickListener { _, _, position, _ ->
                    selectedCategory = categoryNames[position]
                }
            }

            if (kostumId != -1) loadKostum()
        }
    }

    private fun loadKostum() {
        kostumRepository.getKostumDetail(kostumId) { kostum ->
            sizeStockPriceData = kostum.sizeStockPrice.toMutableList()
            binding.rvSizeStockPrice.layoutManager = LinearLayoutManager(this@TambahKostumActivity)
            sizeStockPriceAdapter = SizeStockPriceAdapter(sizeStockPriceData, { index, data ->
                syncUnit(index, data)
            }, {
                deleteUnit(it)
            })
            binding.rvSizeStockPrice.adapter = sizeStockPriceAdapter

            binding.etName.setText(kostum.name)
            binding.etDescription.setText(kostum.description)

            selectedCategory = categoryList.find { it.id == kostum.categoryId }!!.name
            binding.autoCompleteCategory.setText(selectedCategory, false)

            kostumRepository.getKostumImage(kostum.image) { bitmap ->
                oldBitmap = bitmap
                binding.image = BitmapDrawable(binding.root.resources, bitmap)
            }
        }
    }

    private fun tambahUnit() {
        val data = SizeStockPrice("", 0, 0)
        sizeStockPriceData.add(data)
        sizeStockPriceAdapter.updateData(sizeStockPriceData)
    }

    private fun syncUnit(index: Int, data: SizeStockPrice) {
        sizeStockPriceData[index] = data
    }

    private fun deleteUnit(index: Int) {
        sizeStockPriceData.removeAt(index)
        sizeStockPriceAdapter.updateData(sizeStockPriceData)
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.etName.text.trim().toString().isEmpty()) {
            binding.etName.error = "Nama harus diisi"
            isValid = false
        }

        if (binding.etDescription.text.trim().toString().isEmpty()) {
            binding.etDescription.error = "Deskripsi harus diisi"
            isValid = false
        }

        if (selectedCategory.trim().isEmpty()) {
            binding.autoCompleteCategory.error = "Kategori harus dipilih"
            isValid = false
        }

        if (oldBitmap == null && newBitmap == null) {
            MessageHelper.errorResult(binding.root, "Gambar kostum harus diupload")
            isValid = false
        }

        return isValid
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this@TambahKostumActivity, Manifest.permission.CAMERA)
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
                        val inputStream = this@TambahKostumActivity.contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        newBitmap = bitmap
                        binding.image = BitmapDrawable(binding.root.resources, bitmap)
                    }
                }
            }
        }
    }

    private fun navigateToAdmin() {
        Intent(this@TambahKostumActivity, AdminActivity::class.java).apply {
            putExtra("FRAGMENT_INDEX", 1)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }.let {
            startActivity(it)
        }
        finish()
    }
}