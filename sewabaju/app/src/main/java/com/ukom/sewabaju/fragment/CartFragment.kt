package com.ukom.sewabaju.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukom.sewabaju.activity.defaultActivity.KostumDetailActivity
import com.ukom.sewabaju.adapter.recyclerView.CartAdapter
import com.ukom.sewabaju.databinding.FragmentCartBinding
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.model.request.OrderItemRequest
import com.ukom.sewabaju.model.request.OrderRequest
import com.ukom.sewabaju.repository.KostumRepository
import com.ukom.sewabaju.repository.OrderItemRepository
import com.ukom.sewabaju.repository.OrderRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.clearCart
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.getCart
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding
    private lateinit var kostumRepository: KostumRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var orderItemRepository: OrderItemRepository
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    private var total = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        handleEvent()
        loadData()
    }

    private fun initLayout() {
        kostumRepository = binding?.let { KostumRepository(it.root) }!!

        orderRepository = binding?.let { OrderRepository(it.root) }!!
        orderItemRepository = binding?.let { OrderItemRepository(it.root) }!!

        val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        binding?.etStartDate?.setText(formatDate.format(startCalendar.time))
        binding?.etEndDate?.setText(formatDate.format(endCalendar.time))
    }

    private fun handleEvent() {
        binding?.etStartDate?.setOnClickListener {
            showDatePickerDialog(binding!!.etStartDate, startCalendar)
        }
        binding?.etEndDate?.setOnClickListener {
            showDatePickerDialog(binding!!.etEndDate, endCalendar)
        }
        binding?.btnCheckout?.setOnClickListener {
            MessageHelper.customAlert(
                binding!!.root,
                "Checkout",
                "Yakin ingin melakukan checkout?",
                { checkout() },
                { }
            )
        }
    }

    private fun checkout() {
        if (requireContext().getCart().length() <= 0) {
            MessageHelper.errorResult(binding!!.root, "Tidak dapat checkout karena cart kosong")
            return
        }

        val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        orderRepository.insertOrder(
            OrderRequest(
                formatDate.format(startCalendar.time),
                formatDate.format(endCalendar.time),
                total.toString(),
                "pending",
                "unpaid"
            ),
            onSuccess = { order ->
                val items = requireContext().getCart()
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    orderItemRepository.insertOrderItem(
                        OrderItemRequest(
                            order.id,
                            item.getInt("id"),
                            item.getString("size"),
                            item.getInt("price").toString(),
                            item.getInt("amount")
                        )
                    ) {  }
                }

                requireContext().clearCart()
                loadData()
            }, { }
        )
    }

    private fun showDatePickerDialog(editText: EditText, calendar: Calendar) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            calendar.set(selectedYear, selectedMonth, selectedDay)
            editText.setText(formatDate.format(calendar.time))
        }, year, month - 1, day).show()
    }

    private fun loadData() {
        val items = requireContext().getCart()
        Log.e("CartFragment", items.toString())
        binding?.rvCart?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvCart?.adapter = CartAdapter(items, kostumRepository, {
            loadData()
        }, {
            Intent(requireContext(), KostumDetailActivity::class.java).apply {
                putExtra("kostumId", it)
            }.let {
                startActivity(it)
            }
        })

        calculateTotal(items)
    }

    private fun calculateTotal(items: JSONArray) {
        total = 0

        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)
            total += item.getInt("subTotal")
        }

        binding?.total = total.toString()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}