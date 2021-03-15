package com.example.githubuser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.User
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.databinding.ItemGridBinding
import com.example.githubuser.ui.DetailActivity
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: ItemGridBinding
    private var listData: ArrayList<User> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding2 = ItemGridBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        binding.rvUser.setHasFixedSize(true)

        getJsonData(this, "githubuser.json")
        showRecycler()
    }

    fun getJsonData(context: Context, fileName: String){
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val objUser = JSONObject(jsonString)
            val users = objUser.getJSONArray("users")

            for(i in 0 .. (users.length() - 1)) {
                with(users.getJSONObject(i)) {
                    val imageResource: Int = resources.getIdentifier(getString("avatar"), null, packageName)
                    listData.add(
                        User(
                            name = getString("name"),
                            username = getString("username"),
                            avatar = imageResource,
                            follower = getInt("follower"),
                            following = getInt("following"),
                            repository = getInt("repository"),
                            company = getString("company"),
                            location = getString("location")
                        )
                    )
                }
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            finish()
        }
    }

    private fun showRecycler() {
        binding.rvUser.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val listUserAdapter = UserAdapter(listData)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(moveIntent)
            }
        })
    }
}