package com.ukom.sewabaju.activity.defaultActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.ukom.sewabaju.R
import com.ukom.sewabaju.databinding.ActivityAdminBinding
import com.ukom.sewabaju.fragment.CustomerFragment
import com.ukom.sewabaju.fragment.LaporanFragment
import com.ukom.sewabaju.fragment.OrderFragment

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
    }

    private fun initLayout() {
        binding = ActivityAdminBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val index = intent.getIntExtra("FRAGMENT_INDEX", -1)
        when (index) {
            1 -> {
                openFragment(CustomerFragment())
            }
            else -> {
                openFragment(OrderFragment())
            }
        }
    }

    private fun handleEvent() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_navigation_transaksi -> openFragment(OrderFragment())
                R.id.admin_navigation_master -> openFragment(CustomerFragment())
                R.id.admin_navigation_laporan -> openFragment(LaporanFragment())
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        clearFragment()

        if (fragment is CustomerFragment) {
            fragment.arguments = Bundle().apply {
                putString("role", "admin")
            }
        }

        supportFragmentManager.beginTransaction()
            .add(binding.fmAdmin.id, fragment)
            .commit()
    }

    private fun clearFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        for (fragment in supportFragmentManager.fragments) {
            fragmentTransaction.remove(fragment)
        }
        fragmentTransaction.commitNowAllowingStateLoss()
    }
}