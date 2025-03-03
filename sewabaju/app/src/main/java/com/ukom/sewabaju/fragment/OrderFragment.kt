package com.ukom.sewabaju.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.ukom.sewabaju.adapter.viewPager.OrderViewPagerAdapter
import com.ukom.sewabaju.databinding.FragmentOrderBinding
import com.ukom.sewabaju.repository.OrderRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.getCustomData

class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding
    private lateinit var orderRepository: OrderRepository
    private lateinit var pagerAdapter: OrderViewPagerAdapter
    private val role by lazy { requireContext().getCustomData("role") }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        handleEvent()
    }

    private fun initLayout() {
        orderRepository = binding?.let { OrderRepository(it.root) }!!

        if (role == "admin") {
            binding?.etSearch?.visibility = View.VISIBLE
        } else {
            binding?.etSearch?.visibility = View.GONE
        }

        val orderStatusList = listOf("pending", "confirmed", "completed", "canceled")
        pagerAdapter = OrderViewPagerAdapter(childFragmentManager, lifecycle, orderStatusList)
        binding?.orderPager?.adapter = pagerAdapter
        binding?.orderPager?.offscreenPageLimit = 1

        if (binding != null) {
            TabLayoutMediator(binding!!.tlOrderStatus, binding!!.orderPager) { tab, position ->
                tab.text = orderStatusList[position]
            }.attach()
        }
    }

    private fun handleEvent() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            pagerAdapter.loadData(binding?.etSearch?.text.toString())
        }
        binding?.etSearch?.addTextChangedListener {
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 1000)
        }
    }

    override fun onResume() {
        super.onResume()
        pagerAdapter.loadData(binding?.etSearch?.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}