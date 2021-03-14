package com.example.githubuser.adapter

import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.MainActivity
import com.example.githubuser.data.User
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.DetailActivity

class UserAdapter(val listUser: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    inner class ListViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding: ItemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            val iv_context = itemView.context
            with(listUser[position]) {
                Glide.with(iv_context)
                        .load(avatar)
                        .into(binding.itemUserPhoto)
                binding.itemUserName.text = name
                binding.itemUserUsername.text = username

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(listUser[position])
                }
            }
        }
    }
    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}