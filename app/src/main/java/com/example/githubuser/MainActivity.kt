package com.example.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.User
import com.example.githubuser.data.UserData
import com.example.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var list: ArrayList<User> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
//        supportActionBar?.title = "GithubUser"

//        binding.rvUser.setHasFixedSize(true)
        list.addAll(UserData.listData)

        showRecycler()
    }

    private fun showRecycler() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listDogAdapter = UserAdapter(list)
        binding.rvUser.adapter = listDogAdapter
    }
}