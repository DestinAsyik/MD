package com.id.destinasyik.ui.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailPagerAdapter(
    activity: AppCompatActivity
): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment()
            1 -> ReviewFragment()
            2 -> PricingFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }

}