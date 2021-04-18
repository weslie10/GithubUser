package com.example.githubuser.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.model.user.User
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.favorite.Favorite
import com.example.githubuser.model.favorite.FavoriteDatabase
import com.example.githubuser.view.activity.FavoriteActivity.Companion.CONTENT_URI
import com.example.githubuser.view.adapter.SectionsPagerAdapter
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.viewmodel.FavoriteViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var fav: Favorite
    var favorite: ArrayList<Favorite> = arrayListOf()

    companion object {
        const val EXTRA_USER = "extra-user"
        @StringRes
        private val TAB_TITLES = arrayListOf(
            R.plurals.follower,
            R.plurals.following,
            R.plurals.repo
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
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        setTabLayout()
        showLoadingDetail(true)
        val username = intent.getParcelableExtra<User>(EXTRA_USER).username as String
        detailViewModel.getDetail(username)
        setData()

        cekFavorite()
        binding.favorite.setOnClickListener {
            binding.status.text = if(binding.status.text == "true") "false" else "true"
            if(binding.status.text.toString().toBoolean()) {
                favoriteViewModel.addFavorite(fav)
            }
            else {
                favoriteViewModel.deleteFavorite(fav.username)
            }
            it.context?.contentResolver?.notifyChange(CONTENT_URI, null)
            setFavorite()
        }
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

    private fun setData() {
        detailViewModel.getUser().observe(this, { userItems ->
            if (userItems != null) {
                fav = Favorite(username = userItems.username as String,avatar = userItems.avatar as String)
                showLoadingDetail(false)
                with(binding) {
                    Glide.with(this@DetailActivity)
                        .load(userItems.avatar)
                        .into(detailPhoto)

                    detailName.text = validateString(userItems.name as String)
                    detailUsername.text = userItems.username

                    detailRepositories.text = userItems.repository.toString()
                    tvRepositories.text = resources.getQuantityText(R.plurals.repo, userItems.repository as Int)

                    detailFollowers.text = userItems.follower.toString()
                    tvFollowers.text = resources.getQuantityText(R.plurals.follower, userItems.follower as Int)

                    detailFollowing.text = userItems.following.toString()
                    tvFollowing.text = resources.getQuantityText(R.plurals.following, userItems.following as Int)

                    detailCompany.text = validateString(userItems.company as String)
                    detailLocation.text = validateString(userItems.location as String)

                }
            }
        })
    }

    private fun setFavorite() {
        if(binding.status.text.toString().toBoolean()) {
            binding.favorite.setImageResource(R.drawable.ic_favorite_24)
        }
        else {
            binding.favorite.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }

    private fun cekFavorite() {
        val favDao = FavoriteDatabase.getInstance(application).favDao()
        val name = intent.getParcelableExtra<User>(EXTRA_USER).username as String
        var list: List<Favorite>
        lifecycleScope.launch(Dispatchers.IO) {
            list = favDao.getByUsername(name)
            for(item in list) {
                if(item.username == name) {
                    favorite.add(item)
                }
            }
            binding.status.text = if(favorite.size == 0)
                "false"
            else
                "true"

            setFavorite()
        }
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
                delay(1000L)
                withContext(Dispatchers.Main) {
                    binding.progressBarDetail.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}