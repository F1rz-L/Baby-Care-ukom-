package com.konyol.babycarex.activity.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.konyol.babycarex.R
import com.konyol.babycarex.data.network.BarangApiService
import com.konyol.babycarex.data.network.PelangganApiService
import com.konyol.babycarex.data.network.PinjamanApiService
import com.konyol.babycarex.databinding.ActivityStrukBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject
import kotlin.math.max

@AndroidEntryPoint
class StrukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStrukBinding

    @Inject lateinit var pinjamanApiService: PinjamanApiService
    @Inject lateinit var pelangganApiService: PelangganApiService
    @Inject lateinit var barangApiService: BarangApiService

    private val pinjamanId by lazy { intent.getIntExtra("pinjamanId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStrukBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handleEvent()
        loadData()
    }

    private fun handleEvent() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnKirimKeWhatsapp.setOnClickListener {
//            NotaImageBuilder(order, kostumRepository).generateAndShareReceipt(this@NotaActivity)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            val pinjaman = pinjamanApiService.getPinjamanById(pinjamanId).body()?.data
            val barangDipinjam = barangApiService.getBarangById(pinjaman!!.id_barang).body()?.data
            val user = pinjaman!!.id_peminjam.let { pelangganApiService.getPelangganById(it).body()!!.data }

            val tglPinjam = LocalDate.parse(pinjaman.tgl_pinjam)
            val tglKembali = LocalDate.parse(pinjaman.tgl_kembali)
            val daysDifference = ChronoUnit.DAYS.between(tglPinjam, tglKembali)
            val weeksDifference = max(1, (daysDifference + 6) / 7)
            val totalHarga = barangDipinjam?.harga?.times(weeksDifference) ?: 0

            val sbHeader = StringBuilder()
            val sbFooter = StringBuilder()

            val parseDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

//            sbHeader.append("Faktur: ").append(pinjaman.id).append("\n")
            sbHeader.append("Pelanggan: ").append(user.nama).append("\n")
            sbHeader.append("Alamat: ").append(user.alamat).append("\n")
            sbHeader.append("Telepon: ").append(user.nomor_telepon).append("\n")
            sbHeader.append("Tanggal Awal: ").append(formatDate.format(parseDate.parse(pinjaman.tgl_pinjam)!!)).append("\n")
            sbHeader.append("Tanggal Akhir: ").append(formatDate.format(parseDate.parse(pinjaman.tgl_kembali)!!)).append("\n")

            when(pinjaman.status) {
                "Dipinjam" -> sbHeader.append("Status: Dipinjam").append("\n")
                "Dikembalikan" -> sbHeader.append("Status: Selesai").append("\n")
                else -> sbHeader.append("Status: Dibatalkan").append("\n")
            }

            sbFooter.append("Total: ").append(totalHarga).append("\n")
            sbFooter.append("Denda: ").append(pinjaman.denda).append("\n")

            binding.headerOrder.text = sbHeader.toString()
            binding.footerOrder.text = sbFooter.toString()

            binding.tvNamaBarang.text = barangDipinjam?.namabarang
            binding.tvHargaAwal.text = barangDipinjam?.harga.toString()
            binding.tvMingguPinjam.text = weeksDifference.toString()
            binding.tvTotalHarga.text = totalHarga.toString()
        }
    }
}