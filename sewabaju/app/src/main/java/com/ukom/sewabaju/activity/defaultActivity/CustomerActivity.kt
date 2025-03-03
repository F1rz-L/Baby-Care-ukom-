package com.ukom.sewabaju.activity.defaultActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.ukom.sewabaju.R
import com.ukom.sewabaju.databinding.ActivityCustomerBinding
import com.ukom.sewabaju.fragment.CartFragment
import com.ukom.sewabaju.fragment.CustomerFragment
import com.ukom.sewabaju.fragment.OrderFragment

class CustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        handleEvent()
    }

    private fun initLayout() {
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        openFragment(CustomerFragment())
    }

    private fun handleEvent() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.customer_navigation_dashboard -> openFragment(CustomerFragment())
                R.id.customer_navigation_cart -> openFragment(CartFragment())
                R.id.customer_navigation_order -> openFragment(OrderFragment())
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment, role: String = "") {
        clearFragment()

        if (fragment is CustomerFragment) {
            fragment.arguments = Bundle().apply {
                putString("role", "customer")
            }
        }

        supportFragmentManager.beginTransaction()
            .add(binding.fmCustomer.id, fragment)
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