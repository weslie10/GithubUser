package com.example.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.User
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.activity.DetailActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter

    companion object {
        const val URL = "https://api.github.com"
        const val TOKEN = "token 432e3f2607946796b8dfe0e4540a9c0d28cb3dd5"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        getAll()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                getSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("query",newText)
//                showLoading(true)
//                getSearch(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecycler() {
        showLoading(false)
        binding.rvUser.setHasFixedSize(true)

        binding.rvUser.layoutManager = GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(moveIntent)
            }
        })
    }

    fun getAll() {
        showLoading(true)
        var listData: ArrayList<User> = arrayListOf()
        AndroidNetworking.get("${URL}/users")
            .addHeaders("Authorization", TOKEN)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        for (i in 0..(response.length() - 1)) {
                            with(response.getJSONObject(i)) {
                                listData.add(
                                    User(
                                        getString("login"),
                                        getString("avatar_url")
                                    )
                                )
                            }
                        }
                        adapter.setData(listData)
                        showRecycler()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(error: ANError) {
                    error.printStackTrace()
                }
            })
    }

    fun getSearch(name: String) {
        var listData = arrayListOf<User>()
        AndroidNetworking.get("${URL}/search/users?q=${name}")
            .addHeaders("Authorization", TOKEN)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val count = response.getInt("total_count")
                        if(count > 0) {
                            val data = response.getJSONArray("items")
                            for (i in 0..(data.length() - 1)) {
                                with(data.getJSONObject(i)) {
                                    listData.add(
                                        User(
                                            getString("login"),
                                            getString("avatar_url")
                                        )
                                    )
                                }
                            }
                            adapter.setData(listData)
                            showRecycler()
                        }
                        else {
                            Toast.makeText(this@MainActivity, resources.getString(R.string.no_match),Toast.LENGTH_LONG).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(error: ANError) {
                    error.printStackTrace()
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