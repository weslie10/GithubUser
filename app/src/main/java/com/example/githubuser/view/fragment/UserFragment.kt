package com.example.githubuser.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.view.adapter.UserDetailAdapter
import com.example.githubuser.databinding.FragmentUserBinding
import com.example.githubuser.model.user.User
import com.example.githubuser.view.activity.DetailActivity
import com.example.githubuser.view.adapter.RepoAdapter
import com.example.githubuser.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: UserDetailAdapter
    private lateinit var adapterRepo: RepoAdapter
    private lateinit var mainViewModel: MainViewModel

    private var notFound = false

    companion object {
        private const val NAME = "name"
        private const val TYPE = "type"

        @JvmStatic
        fun newInstance(name: String?, type: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, name)
                    putString(TYPE, type)
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        adapter = UserDetailAdapter()
        adapterRepo = RepoAdapter()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        recyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)
        val name = arguments?.getString(NAME)
        val type = arguments?.getString(TYPE)
        showLoading(true)
        if (type == "repository") {
            mainViewModel.repo(name)
        }
        else {
            mainViewModel.follow(name, type)
        }
        setData(type)
    }

    private fun recyclerView() {
        adapter.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(context)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : UserDetailAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveIntent = Intent(activity, DetailActivity::class.java).apply{
                    putExtra(DetailActivity.EXTRA_USER, data)
                }
                startActivity(moveIntent)
            }
        })
    }

    private fun recyclerViewRepo() {
        adapterRepo.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(context)
        binding.rvUser.adapter = adapterRepo
    }

    private fun setData(type: String?) {
        mainViewModel.getState().observe(viewLifecycleOwner, { state->
            if(state) {
                binding.notFound.visibility = View.GONE
                if(type == "repository") {
                    mainViewModel.getRepo().observe(viewLifecycleOwner, { userItems ->
                        if (userItems != null) {
                            showLoading(false)
                            adapterRepo.setData(userItems)
                            recyclerViewRepo()
                        }
                    })
                }
                else {
                    mainViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
                        if (userItems != null) {
                            showLoading(false)
                            adapter.setData(userItems)
                            recyclerView()
                        }
                    })
                }
            }
            else {
                binding.notFound.text = "${resources.getString(R.string.not_found)} ${type}"
                notFound = true
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvUser.visibility = View.GONE
        } else {
            lifecycleScope.launch(Dispatchers.Default) {
                delay(1500L)
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.rvUser.visibility = View.VISIBLE
                    if(notFound) {
                        binding.notFound.visibility = View.VISIBLE
                        binding.rvUser.visibility = View.GONE
                    }
                }
            }
        }
    }
}