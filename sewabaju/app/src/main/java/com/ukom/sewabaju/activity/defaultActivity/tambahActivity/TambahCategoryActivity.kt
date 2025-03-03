package com.ukom.sewabaju.activity.defaultActivity.tambahActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukom.sewabaju.R
import com.ukom.sewabaju.activity.defaultActivity.AdminActivity
import com.ukom.sewabaju.adapter.recyclerView.CategoryAdapter
import com.ukom.sewabaju.databinding.ActivityTambahCategoryBinding
import com.ukom.sewabaju.model.request.CategoryRequest
import com.ukom.sewabaju.repository.CategoryRepository

class TambahCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahCategoryBinding
    private lateinit var categoryRepository: CategoryRepository
    private var currentCategoryId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
        loadData()
    }

    private fun initLayout() {
        binding = ActivityTambahCategoryBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        categoryRepository = CategoryRepository(binding.root)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener { navigateToAdmin() }
        binding.btnTambah.setOnClickListener { saveData() }
        binding.btnBatal.setOnClickListener { clear() }
    }

    private fun loadData() {
        categoryRepository.getCategories { categories ->
            binding.rvCategory.layoutManager = LinearLayoutManager(this@TambahCategoryActivity)
            binding.rvCategory.adapter = CategoryAdapter(categories, { editData(it) }, { deleteData(it) })
        }
    }

    private fun saveData() {
        if (!validation()) return

        binding.btnTambah.isEnabled = false
        if (currentCategoryId == -1) {
            categoryRepository.insertCategory(
                CategoryRequest(
                    binding.etName.text.toString(),
                    binding.etDescription.text.toString()
                ), {
                    binding.btnTambah.isEnabled = true
                    loadData()
                    clear()
                }, { binding.btnTambah.isEnabled = true }
            )
        } else {
            categoryRepository.updateCategory(currentCategoryId,
                CategoryRequest(
                    binding.etName.text.toString(),
                    binding.etDescription.text.toString()
                ), {
                    binding.btnTambah.isEnabled = true
                    loadData()
                    clear()
                }, { binding.btnTambah.isEnabled = true }
            )
        }
    }

    private fun editData(id: Int) {
        currentCategoryId = id
        binding.btnTambah.text = "Perbarui"

        categoryRepository.getCategoryDetail(id) {
            binding.etName.setText(it.name)
            binding.etDescription.setText(it.description)
        }
    }

    private fun deleteData(id: Int) {
        categoryRepository.deleteCategory(id) {
            loadData()
        }
    }

    private fun clear() {
        currentCategoryId = -1
        binding.etName.setText("")
        binding.etDescription.setText("")
        binding.btnTambah.text = "Tambah"
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

        return isValid
    }

    override fun onPause() {
        super.onPause()
        navigateToAdmin()
    }

    private fun navigateToAdmin() {
        Intent(this@TambahCategoryActivity, AdminActivity::class.java).apply {
            putExtra("FRAGMENT_INDEX", 1)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }.let {
            startActivity(it)
        }
        finish()
    }
}