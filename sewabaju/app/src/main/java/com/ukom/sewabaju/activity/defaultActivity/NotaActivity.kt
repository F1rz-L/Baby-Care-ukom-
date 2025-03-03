package com.ukom.sewabaju.activity.defaultActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukom.sewabaju.R
import com.ukom.sewabaju.adapter.recyclerView.NotaItemAdapter
import com.ukom.sewabaju.databinding.ActivityNotaBinding
import com.ukom.sewabaju.helper.NotaImageBuilder
import com.ukom.sewabaju.model.Order
import com.ukom.sewabaju.repository.KostumRepository
import com.ukom.sewabaju.repository.OrderRepository
import java.text.SimpleDateFormat
import java.util.Locale

class NotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotaBinding
    private lateinit var orderRepository: OrderRepository
    private lateinit var kostumRepository: KostumRepository
    private val orderId by lazy { intent.getIntExtra("orderId", -1) }
    private lateinit var order: Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
        loadData()
    }

    private fun initLayout() {
        binding = ActivityNotaBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        orderRepository = OrderRepository(binding.root)
        kostumRepository = KostumRepository(binding.root)
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
        binding.btnKirimKeWhatsapp.setOnClickListener {
            NotaImageBuilder(order, kostumRepository).generateAndShareReceipt(this@NotaActivity)
        }
    }

    private fun loadData() {
        orderRepository.getOrder(orderId) { order ->
            this.order = order
            binding.rvNotaItem.layoutManager = LinearLayoutManager(this@NotaActivity)
            binding.rvNotaItem.adapter = NotaItemAdapter(order.orderItem!!, kostumRepository)

            val user = order.user!!

            val sbHeader = StringBuilder()
            val sbFooter = StringBuilder()

            val parseDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            sbHeader.append("Faktur: ").append(order.faktur).append("\n")
            sbHeader.append("Pelanggan: ").append(user.name).append("\n")
            sbHeader.append("Alamat: ").append(user.address).append("\n")
            sbHeader.append("Telepon: ").append(user.phoneNumber).append("\n")
            sbHeader.append("Tanggal Awal: ").append(formatDate.format(parseDate.parse(order.rentalStartDate)!!)).append("\n")
            sbHeader.append("Tanggal Akhir: ").append(formatDate.format(parseDate.parse(order.rentalEndDate)!!)).append("\n")

            when(order.orderStatus) {
                "pending" -> sbHeader.append("Status: Proses").append("\n")
                "confirmed" -> sbHeader.append("Status: Dipinjam").append("\n")
                "completed" -> sbHeader.append("Status: Selesai").append("\n")
                else -> sbHeader.append("Status: Dibatalkan").append("\n")
            }

            sbFooter.append("Subtotal: ").append(order.totalPrice).append("\n")
            sbFooter.append("Total: ").append(order.totalPrice).append("\n")

            when(order.paymentStatus) {
                "paid" -> sbFooter.append("Status Pembayaran: Sudah Dibayar")
                else -> sbFooter.append("Status Pembayaran: Belum Dibayar")
            }

            binding.headerOrder.text = sbHeader.toString()
            binding.footerOrder.text = sbFooter.toString()
        }
    }
}