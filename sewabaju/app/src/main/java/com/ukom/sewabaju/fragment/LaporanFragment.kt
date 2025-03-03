package com.ukom.sewabaju.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.ukom.sewabaju.databinding.FragmentLaporanBinding
import com.ukom.sewabaju.repository.LaporanRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LaporanFragment : Fragment() {
    private var _binding: FragmentLaporanBinding? = null
    private val binding get() = _binding
    private lateinit var laporanRepository: LaporanRepository
    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaporanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        handleEvent()
        loadData()
    }

    private fun initLayout() {
        laporanRepository = binding?.let { LaporanRepository(it.root) }!!

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
    }

    private fun loadData() {
        if (isDetached) return
        if (!isAdded) return

        val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        laporanRepository.getLaporan(formatDate.format(startCalendar.time), formatDate.format(endCalendar.time)) {
            binding?.orderMasuk = it.orderMasuk
            binding?.orderPending = it.orderProses
            binding?.orderConfirmed = it.orderMasaPinjam
            binding?.orderCompleted = it.orderSelesai
            binding?.orderCanceled = it.orderBatal
            binding?.totalIncome = it.totalPemasukan
        }
    }

    private fun showDatePickerDialog(editText: EditText, calendar: Calendar) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            calendar.set(selectedYear, selectedMonth, selectedDay)
            editText.setText(formatDate.format(calendar.time))
            loadData()
        }, year, month - 1, day).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}