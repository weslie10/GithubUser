package com.example.githubuser.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuser.data.User
import com.example.githubuser.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    companion object {
        const val EXTRA_USER = "extra-user"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val user: User = intent.getParcelableExtra(EXTRA_USER)

        Glide.with(this)
            .load(user.avatar)
            .into(binding.detailPhoto)
        binding.detailName.text = user.name
        binding.detailUsername.text = user.username
        binding.detailRepositories.text = user.repository.toString()
        binding.detailFollowers.text = user.follower.toString()
        binding.detailFollowing.text = user.following.toString()
        binding.detailCompany.text = user.company
        binding.detailLocation.text = user.location
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}