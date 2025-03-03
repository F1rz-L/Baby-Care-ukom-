package com.ukom.sewabaju.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ukom.sewabaju.fragment.DynamicOrderFragment

class OrderViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var orderStatusList: List<String>
): FragmentStateAdapter(fragmentManager, lifecycle) {
    private var fragments: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int = orderStatusList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = DynamicOrderFragment.newInstance(orderStatusList[position])
        fragments.add(fragment)
        return fragment
    }

    fun loadData(search: String) {
        fragments.forEach {
            (it as DynamicOrderFragment).loadData(search)
        }
    }
}