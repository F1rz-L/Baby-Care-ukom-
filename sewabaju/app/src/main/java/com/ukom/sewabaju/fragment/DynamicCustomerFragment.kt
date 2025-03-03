package com.ukom.sewabaju.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ukom.sewabaju.activity.defaultActivity.KostumDetailActivity
import com.ukom.sewabaju.adapter.recyclerView.CustomerAdapter
import com.ukom.sewabaju.databinding.FragmentDynamicCustomerBinding
import com.ukom.sewabaju.repository.KostumRepository

private const val ARG_CATEGORY_ID = "categoryId"

class DynamicCustomerFragment : Fragment() {
    private var _binding: FragmentDynamicCustomerBinding? = null
    private val binding get() = _binding
    private lateinit var kostumRepository: KostumRepository
    private var categoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDynamicCustomerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        loadData("")
    }

    private fun initLayout() {
        kostumRepository = binding?.let { KostumRepository(it.root) }!!
    }

    fun loadData(search: String) {
        if (isDetached) return
        if (!isAdded) return

        kostumRepository.getKostumByCategory(categoryId, search) { kostums ->
            binding?.rvKostum?.layoutManager = GridLayoutManager(requireContext(), 2)
            binding?.rvKostum?.adapter = CustomerAdapter(kostums, kostumRepository) {
                Intent(requireContext(), KostumDetailActivity::class.java).apply {
                    putExtra("kostumId", it)
                }.let {
                    startActivity(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryId: Int) =
            DynamicCustomerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CATEGORY_ID, categoryId)
                }
            }
    }
}