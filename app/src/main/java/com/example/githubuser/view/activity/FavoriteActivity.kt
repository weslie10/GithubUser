package com.example.githubuser.view.activity

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.model.favorite.Favorite
import com.example.githubuser.model.user.User
import com.example.githubuser.other.helper.MappingHelper
import com.example.githubuser.view.adapter.FavoriteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    companion object {
        const val AUTHORITY = "com.example.githubuser"
        private const val SCHEME = "content"
        const val TABLE_NAME = "favorite"
        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = resources.getString(R.string.favorite_user)
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        recyclerView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        showData()

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                showData()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        showData()
    }

    private fun recyclerView() {
        adapter = FavoriteAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFav.setHasFixedSize(true)
        binding.rvFav.layoutManager = LinearLayoutManager(this)
        binding.rvFav.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favorite) {
                val user = User(username=data.username, avatar=data.avatar)
                val moveIntent = Intent(this@FavoriteActivity, DetailActivity::class.java).apply{
                    putExtra(DetailActivity.EXTRA_USER, user)
                }
                startActivity(moveIntent)
            }
        })
    }

    private fun showData() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorite = deferredFavorite.await()
            if (favorite.size > 0) {
                adapter.setData(favorite)
            } else {
                adapter.setData(arrayListOf())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val favorite: MenuItem =  menu.findItem(R.id.favorite)
        favorite.isVisible = false
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val mIntent = Intent(this, SettingActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}