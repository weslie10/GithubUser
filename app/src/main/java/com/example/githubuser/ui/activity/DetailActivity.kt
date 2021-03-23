package com.example.githubuser.ui.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.example.githubuser.MainActivity.Companion.TOKEN
import com.example.githubuser.MainActivity.Companion.URL
import com.example.githubuser.R
import com.example.githubuser.data.User
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.ui.fragment.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONException
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    companion object {
        const val EXTRA_USER = "extra-user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.plurals.follower,
            R.plurals.following
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Detail User"
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        showLoadingDetail(true)
        setTabLayout()
        getDetail()
    }

    private fun setTabLayout(){
        val name: User = intent.getParcelableExtra(EXTRA_USER)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.name = name.username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getQuantityText(TAB_TITLES[position], 1)
        }.attach()
    }

    fun getDetail() {
        val name: User = intent.getParcelableExtra(EXTRA_USER)
        AndroidNetworking.get("$URL/users/${name.username}")
            .addHeaders("Authorization", TOKEN)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        with(response) {
                            with(binding) {
                                Glide.with(applicationContext)
                                    .load(getString("avatar_url"))
                                    .into(detailPhoto)

                                detailName.text = validateString(getString("name"))
                                detailUsername.text = getString("login")

                                detailRepositories.text = getString("public_repos")
                                tvRepositories.text = resources.getQuantityText(R.plurals.repo, getInt("public_repos"))

                                detailFollowers.text = getString("followers")
                                tvFollowers.text = resources.getQuantityText(R.plurals.follower, getInt("followers"))

                                detailFollowing.text = getString("following")
                                tvFollowing.text = resources.getQuantityText(R.plurals.following, getInt("following"))

                                detailCompany.text = validateString(getString("company"))
                                detailLocation.text = validateString(getString("location"))
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    showLoadingDetail(false)
                }

                override fun onError(error: ANError) {
                    error.printStackTrace()
                }
            })
    }

    private fun validateString(str: String): String {
        if(str != "null")  return str
        else return resources.getString(R.string.emptyData)
    }

    private fun showLoadingDetail(state: Boolean) {
        binding.progressBarDetail.visibility =
            if(state)
                View.VISIBLE
            else
                View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}