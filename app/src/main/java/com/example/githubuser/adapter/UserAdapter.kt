package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.data.User
import com.example.githubuser.databinding.ItemGridBinding

class UserAdapter(val listUser: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    inner class ListViewHolder(val binding: ItemGridBinding): RecyclerView.ViewHolder(binding.root)
    override fun getItemCount(): Int = listUser.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding: ItemGridBinding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            with(listUser[position]) {
                Glide.with(itemView.context)
                        .load(avatar)
                        .apply(RequestOptions().override(300).centerCrop())
                        .into(binding.itemUserPhoto)
                binding.itemUserName.text = name
                binding.itemUserUsername.text = username

                itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[position]) }
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}