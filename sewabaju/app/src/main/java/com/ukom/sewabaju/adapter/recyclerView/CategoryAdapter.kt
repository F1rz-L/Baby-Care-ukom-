package com.ukom.sewabaju.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ukom.sewabaju.databinding.ListCategoryBinding
import com.ukom.sewabaju.model.Category

class CategoryAdapter(
    private val items: List<Category>,
    private val editData: (id: Int) -> Unit,
    private val deleteData: (id: Int) -> Unit
): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val binding: ListCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ListCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]

        holder.binding.name = item.name
        holder.binding.description = item.description

        holder.binding.btnEdit.setOnClickListener {
            editData(item.id)
        }

        holder.binding.btnHapus.setOnClickListener {
            deleteData(item.id)
        }
    }
}