package com.example.githubuser.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.R
import com.example.githubuser.view.adapter.UserAdapter
import com.example.githubuser.model.user.User
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private var animState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = GridLayoutManager(this, 2)

        showLoading(true)
        searchListener()
        mainViewModel.all()
        setData()
        recycler()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var mIntent: Intent? = null
        when (item.itemId) {
            R.id.favorite -> {
                mIntent = Intent(this, FavoriteActivity::class.java)
            }
            R.id.settings -> {
                mIntent = Intent(this, SettingActivity::class.java)
            }
        }
        if(mIntent!=null)   startActivity(mIntent)

        return super.onOptionsItemSelected(item)
    }

    private fun searchListener() {
        mainViewModel.search.postValue("")
        binding.searchView.apply{
            setOnClickListener {
                onActionViewExpanded()
                showLoading(false)
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.cancelRequest("req")
                    showLoading(true)
                    mainViewModel.findByUsername(query)
                    mainViewModel.setSearch(query)
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    mainViewModel.cancelRequest("req")
                    if(newText.isNotEmpty()) {
                        showLoading(true)
                        mainViewModel.findByUsername(newText)
                        mainViewModel.setSearch(newText)
                    } else {
                        mainViewModel.all()
                    }
                    return true
                }
            })
        }
        mainViewModel.getSearch().observe(this, { search ->
            if (search != "") {
                mainViewModel.findByUsername(search)
            }
            else {
                mainViewModel.all()
            }
        })
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
                animState = true
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            binding.rvUser.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            lifecycleScope.launch(Dispatchers.Default) {
                delay(1000L)
                withContext(Dispatchers.Main) {
                    binding.rvUser.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    if(animState) {
                        animState = false
                        binding.rvUser.visibility = View.GONE
                    }
                }
            }
        }
    }
}