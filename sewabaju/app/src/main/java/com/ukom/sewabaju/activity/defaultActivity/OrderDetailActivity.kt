package com.ukom.sewabaju.activity.defaultActivity

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukom.sewabaju.R
import com.ukom.sewabaju.adapter.recyclerView.CustomerAdapter
import com.ukom.sewabaju.adapter.recyclerView.OrderItemAdapter
import com.ukom.sewabaju.databinding.ActivityOrderDetailBinding
import com.ukom.sewabaju.helper.MessageHelper
import com.ukom.sewabaju.model.Kostum
import com.ukom.sewabaju.model.Order
import com.ukom.sewabaju.model.request.OrderRequest
import com.ukom.sewabaju.repository.KostumRepository
import com.ukom.sewabaju.repository.OrderItemRepository
import com.ukom.sewabaju.repository.OrderRepository
import com.ukom.sewabaju.repository.UserRepository
import com.ukom.sewabaju.sharedpreferences.sharedpreferences.Companion.getCustomData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var kostumRepository: KostumRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var orderItemRepository: OrderItemRepository
    private lateinit var userRepository: UserRepository
    private lateinit var order: Order
    private val orderId by lazy { intent.getIntExtra("orderId", -1) }
    private val role by lazy { getCustomData("role") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initLayout()
        handleEvent()
        loadData()
    }

    private fun initLayout() {
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        userRepository = UserRepository(binding.root)
        kostumRepository = KostumRepository(binding.root)
        orderItemRepository = OrderItemRepository(binding.root)
        orderRepository = OrderRepository(binding.root)

        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBatalkanPeminjaman.visibility = View.GONE
        binding.btnUbahStatusPembayaran.visibility = View.GONE
        binding.btnSelesai.visibility = View.GONE
        binding.btnLihatNota.visibility = View.GONE
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnUbahStatusPembayaran.setOnClickListener {
            updateDialog("Yakin ingin menjadikan pinjaman ini sudah dibayar?") { updateOrder("confirmed", "paid") }
        }
        binding.btnBatalkanPeminjaman.setOnClickListener {
            updateDialog("Yakin ingin membatalkan pinjaman ini?") { updateOrder("canceled", null) }
        }
        binding.btnSelesai.setOnClickListener {
            updateDialog("Yakin ingin menandai pinjaman ini sebagai selesai?") { updateOrder("completed", null) }
        }
        binding.btnLihatNota.setOnClickListener {
            Intent(this@OrderDetailActivity, NotaActivity::class.java).apply {
                putExtra("orderId", orderId)
            }.let {
                startActivity(it)
            }
        }
    }

    private fun updateDialog(message: String, onPositive: () -> Unit) {
        MessageHelper.customAlert(
            binding.root,
            "Ubah Status Pinjaman",
            message,
            { onPositive() },
            { }
        )
    }

    private fun updateOrder(orderStatus: String, paymentStatus: String?) {
        orderRepository.updateOrder(
            orderId,
            OrderRequest(
                order.rentalStartDate,
                order.rentalEndDate,
                order.totalPrice,
                orderStatus,
                paymentStatus ?: order.paymentStatus,
                if (orderStatus == "completed") SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) else null
            ), {
                loadData()
            }, {  }
        )
    }

    private fun loadData() {
        orderRepository.getOrder(orderId) { order ->
            this.order = order
            val user = order.user!!

            userRepository.getUserImage(user.image) { bitmap ->
                binding.image = BitmapDrawable(binding.root.resources, bitmap)
            }

            binding.name = user.name
            binding.email = user.email
            binding.phoneNumber = user.phoneNumber
            binding.address = user.address

            val parseDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

            binding.faktur = order.faktur
            binding.startDate = formatDate.format(parseDate.parse(order.rentalStartDate)!!)
            binding.endDate = formatDate.format(parseDate.parse(order.rentalEndDate)!!)
            binding.orderStatus = order.orderStatus
            binding.paymentStatus = order.paymentStatus

            if (order.returnDate != null) {
                binding.returnDate = formatDate.format(parseDate.parse(order.returnDate)!!)
            } else {
                binding.returnDate = " - "
            }

            binding.total = order.totalPrice

            binding.btnBatalkanPeminjaman.visibility = View.GONE
            binding.btnUbahStatusPembayaran.visibility = View.GONE
            binding.btnSelesai.visibility = View.GONE
            binding.btnLihatNota.visibility = View.GONE

            if (order.orderStatus == "pending" && order.paymentStatus == "unpaid") {
                if (role == "admin") {
                    binding.btnUbahStatusPembayaran.visibility = View.VISIBLE
                }
                binding.btnBatalkanPeminjaman.visibility = View.VISIBLE
            }
            else if (order.orderStatus == "confirmed" && order.paymentStatus == "paid") {
                if (role == "admin") {
                    binding.btnSelesai.visibility = View.VISIBLE
                }
                binding.btnLihatNota.visibility = View.VISIBLE
            }
            else if (order.orderStatus == "completed") {
                binding.btnLihatNota.visibility = View.VISIBLE
            }

            binding.rvOrderDetail.layoutManager = LinearLayoutManager(this@OrderDetailActivity)
            binding.rvOrderDetail.adapter = OrderItemAdapter(order.orderItem!!, kostumRepository)
        }
    }
}