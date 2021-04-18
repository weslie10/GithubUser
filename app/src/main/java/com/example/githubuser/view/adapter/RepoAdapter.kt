package com.example.githubuser.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.databinding.ItemRepoBinding
import com.example.githubuser.model.repository.Repository

class RepoAdapter: RecyclerView.Adapter<RepoAdapter.ListViewHolder>() {
    private val listUser = ArrayList<Repository>()

    fun setData(items: ArrayList<Repository>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemRepoBinding): RecyclerView.ViewHolder(binding.root) {
        var btnRepo: ImageView = binding.btnRepo
        var uri: String? = ""
        fun bind(repo: Repository) {
            with(binding) {
                with(repo) {
                    itemName.text = name
                    itemDescription.text = description
                    uri = url.toString()
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding: ItemRepoBinding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
        holder.btnRepo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(holder.uri)
            holder.btnRepo.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listUser.size
}