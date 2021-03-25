package com.example.githubuser.view.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.model.User
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.view.adapter.SectionsPagerAdapter
import com.example.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

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
            title = resources.getString(R.string.detail_user)
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        setTabLayout()
        showLoadingDetail(true)
        detailViewModel.getDetail(intent.getParcelableExtra<User>(EXTRA_USER).username as String)
        setData()
    }

    private fun setData() {
        detailViewModel.getUser().observe(this, { userItems ->
            if (userItems != null) {
                showLoadingDetail(false)
                with(binding) {
                    Glide.with(this@DetailActivity)
                        .load(userItems.avatar)
                        .into(detailPhoto)

                    detailName.text = validateString(userItems.name as String)
                    detailUsername.text = userItems.username

                    detailRepositories.text = userItems.repository.toString()
                    tvRepositories.text = resources.getQuantityText(R.plurals.repo, if(userItems.repository == 0) 1 else userItems.repository as Int)

                    detailFollowers.text = userItems.follower.toString()
                    tvFollowers.text = resources.getQuantityText(R.plurals.follower, if(userItems.follower == 0) 1 else userItems.follower as Int)

                    detailFollowing.text = userItems.following.toString()
                    tvFollowing.text = resources.getQuantityText(R.plurals.following, if(userItems.following == 0) 1 else userItems.following as Int)

                    detailCompany.text = validateString(userItems.company as String)
                    detailLocation.text = validateString(userItems.location as String)
                }
            }
        })
    }

    private fun setTabLayout(){
        val name: User = intent?.getParcelableExtra(EXTRA_USER) as User

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.name = name.username

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getQuantityText(TAB_TITLES[position], 1)
        }.attach()
    }

    private fun validateString(str: String): String {
        return if(str != "null") {
            str
        } else {
            resources.getString(R.string.emptyData)
        }
    }

    private fun showLoadingDetail(state: Boolean) {
        if(state) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            lifecycleScope.launch(Dispatchers.Default) {
                delay(2000L)
                withContext(Dispatchers.Main) {
                    binding.progressBarDetail.visibility =View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}