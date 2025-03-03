package com.ukom.sewabaju.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ukom.sewabaju.fragment.DynamicCustomerFragment

class CustomerViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var categoryIds: List<Int>
): FragmentStateAdapter(fragmentManager, lifecycle) {
    private var fragments: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int = categoryIds.size

    override fun createFragment(position: Int): Fragment {
        val fragment = DynamicCustomerFragment.newInstance(categoryIds[position])
        fragments.add(fragment)
        return fragment
    }

    fun loadData(search: String) {
        fragments.forEach {
            (it as DynamicCustomerFragment).loadData(search)
        }
    }
}