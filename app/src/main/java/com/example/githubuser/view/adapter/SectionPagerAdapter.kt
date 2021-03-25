package com.example.githubuser.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.view.fragment.UserFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val type = arrayListOf(
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