package com.ukom.sewabaju.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukom.sewabaju.activity.defaultActivity.OrderDetailActivity
import com.ukom.sewabaju.adapter.recyclerView.OrderAdapter
import com.ukom.sewabaju.databinding.FragmentDynamicOrderBinding
import com.ukom.sewabaju.repository.OrderRepository

private const val ARG_ORDER_STATUS = "orderStatus"

class DynamicOrderFragment : Fragment() {
    private var _binding: FragmentDynamicOrderBinding? = null
    private val binding get() = _binding
    private lateinit var orderRepository: OrderRepository
    private var orderStatus: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderStatus = it.getString(ARG_ORDER_STATUS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDynamicOrderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        loadData("")
    }

    private fun initLayout() {
        orderRepository = binding?.let { OrderRepository(it.root) }!!
    }

    fun loadData(namaPelanggan: String) {
        if (isDetached) return
        if (!isAdded) return

        orderRepository.getOrders(orderStatus!!, namaPelanggan) { orders ->
            binding?.rvOrder?.layoutManager = LinearLayoutManager(requireContext())
            binding?.rvOrder?.adapter = OrderAdapter(orders) {
                Intent(requireContext(), OrderDetailActivity::class.java).apply {
                    putExtra("orderId", it)
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
        fun newInstance(orderStatus: String) =
            DynamicOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ORDER_STATUS, orderStatus)
                }
            }
    }
}