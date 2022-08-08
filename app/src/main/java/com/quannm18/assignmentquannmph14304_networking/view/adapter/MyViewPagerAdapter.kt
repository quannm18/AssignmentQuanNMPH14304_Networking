package com.quannm18.assignmentquannmph14304_networking.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quannm18.assignmentquannmph14304_networking.view.fragment.FinishedFragment
import com.quannm18.assignmentquannmph14304_networking.view.fragment.HomeFragment
import com.quannm18.assignmentquannmph14304_networking.view.fragment.UserFragment

class MyViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FinishedFragment()
            }
            1 -> {
                HomeFragment()
            }
            2 -> {
                UserFragment()
            }
            else -> {
                HomeFragment()
            }
        }
    }
}