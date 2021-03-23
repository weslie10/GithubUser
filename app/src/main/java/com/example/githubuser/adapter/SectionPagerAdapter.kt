package com.example.githubuser.ui.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var type = arrayListOf(
        "followers",
        "following"
    )
    var name: String? = null
    override fun createFragment(position: Int): Fragment {
        return UserFragment.newInstance(name, type[position])
    }

    override fun getItemCount(): Int {
        return type.size
    }
}