package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.example.githubuser.data.User
import com.example.githubuser.databinding.ItemUserBinding

class UserAdapter(val listUser: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
//        var name: TextView = itemView.findViewById(R.id.rv_dog_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: ItemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dog = listUser[position]

//        Glide.with(holder.itemView.context)
//            .load(dog.photo)
//            .apply(RequestOptions().override(70,70))
//            .into(holder.img)

//        holder.name.text = dog.name
    }

    override fun getItemCount(): Int = listUser.size
}