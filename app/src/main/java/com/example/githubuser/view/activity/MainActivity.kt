package com.example.githubuser.view.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.R
import com.example.githubuser.view.adapter.UserAdapter
import com.example.githubuser.model.User
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        recycler()

        searchListener()
        showLoading(true)
        mainViewModel.all()
        mainViewModel.getSearch().observe(this, { search ->
            if (search != "") {
                mainViewModel.findByUsername(search)
                setData()
            }
            else {
                setData()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchListener() {
        mainViewModel.search.postValue("")
        binding.searchView.apply{
            setOnClickListener {
                onActionViewExpanded()
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    showLoading(true)
                    mainViewModel.findByUsername(query)
                    mainViewModel.setSearch(query)
                    setData()
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    showLoading(true)
                    if(newText.isNotEmpty()) {
                        mainViewModel.findByUsername(newText)
                        mainViewModel.setSearch(newText)
                    } else {
                        mainViewModel.all()
                    }
                    setData()
                    return true
                }
            })
        }
    }

    private fun recycler() {
        showLoading(false)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveIntent = Intent(this@MainActivity, DetailActivity::class.java).apply{
                    putExtra(DetailActivity.EXTRA_USER, data)
                }
                startActivity(moveIntent)
            }
        })
    }

    fun setData() {
        mainViewModel.getState().observe(this, { state ->
            if(state) {
                mainViewModel.getUser().observe(this, { userItems ->
                    if (userItems != null) {
                        binding.notFound.visibility = View.GONE
                        adapter.setData(userItems)
                        recycler()
                    }
                })
            } else {
                binding.notFound.visibility = View.VISIBLE
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility =
            if(state)
                View.VISIBLE
            else
                View.GONE
    }
}