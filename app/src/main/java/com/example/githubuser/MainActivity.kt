package com.example.githubuser

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.User
import com.example.githubuser.data.UserData
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.DetailActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: ItemUserBinding
    private var list: ArrayList<User> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding2 = ItemUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        actionBar?.title = "Github Users"

        binding.rvUser.setHasFixedSize(true)
        list.addAll(UserData.listData)
        showRecycler()
    }

    private fun showRecycler() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listDogAdapter = UserAdapter(list)
        binding.rvUser.adapter = listDogAdapter

        listDogAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(moveIntent)
            }
        })
    }
}