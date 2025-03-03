package com.ukom.sewabaju.fragment

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.ukom.sewabaju.activity.defaultActivity.ProfileActivity
import com.ukom.sewabaju.activity.defaultActivity.tambahActivity.TambahCategoryActivity
import com.ukom.sewabaju.activity.defaultActivity.tambahActivity.TambahKostumActivity
import com.ukom.sewabaju.adapter.viewPager.CustomerViewPagerAdapter
import com.ukom.sewabaju.databinding.FragmentCustomerBinding
import com.ukom.sewabaju.dialog.OptionDialog
import com.ukom.sewabaju.repository.AuthRepository
import com.ukom.sewabaju.repository.CategoryRepository
import com.ukom.sewabaju.repository.UserRepository

class CustomerFragment : Fragment() {
    private var _binding: FragmentCustomerBinding? = null
    private val binding get() = _binding
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var pagerAdapter: CustomerViewPagerAdapter
    private lateinit var optionDialog: OptionDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        handleEvent()
        loadData()
        loadUser()
    }

    private fun initLayout() {
        authRepository = binding?.let { AuthRepository(it.root) }!!
        userRepository = binding?.let { UserRepository(it.root) }!!
        categoryRepository = binding?.let { CategoryRepository(it.root) }!!
        optionDialog = OptionDialog(requireContext(), { navigateTambahKostum() }, { navigateTambahCategory() })

        if (arguments?.getString("role") == "customer") {
            binding?.btnOpenOption?.visibility = View.GONE
        } else {
            binding?.btnOpenOption?.visibility = View.VISIBLE
        }
    }

    private fun handleEvent() {
        binding?.ivProfile?.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }
        binding?.btnOpenOption?.setOnClickListener {
            optionDialog.show()
        }
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            pagerAdapter.loadData(binding?.etSearch?.text.toString())
        }
        binding?.etSearch?.addTextChangedListener {
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 1000)
        }
    }

    private fun loadData() {
        if (isDetached) return
        if (!isAdded) return

        categoryRepository.getCategories { categories ->
            if (isAdded) {
                pagerAdapter = CustomerViewPagerAdapter(childFragmentManager, lifecycle, categories.map { it.id })
                binding?.customerPager?.adapter = pagerAdapter
                binding?.customerPager?.offscreenPageLimit = 1

                if (binding != null) {
                    TabLayoutMediator(binding!!.tlCategory, binding!!.customerPager) { tab, position ->
                        tab.text = categories[position].name
                    }.attach()
                }
            }
        }
    }

    private fun loadUser() {
        authRepository.me {
            binding?.tvGreet?.text = "Hello, ${it.name}"
            userRepository.getUserImage(it.image) { bitmap ->
                binding?.image = BitmapDrawable(binding?.root?.resources, bitmap)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadUser()
    }

    private fun navigateTambahKostum() {
        startActivity(Intent(requireContext(), TambahKostumActivity::class.java))
    }

    private fun navigateTambahCategory() {
        startActivity(Intent(requireContext(), TambahCategoryActivity::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}