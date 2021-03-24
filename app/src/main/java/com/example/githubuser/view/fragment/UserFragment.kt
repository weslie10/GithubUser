package com.example.githubuser.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.view.adapter.UserDetailAdapter
import com.example.githubuser.databinding.FragmentUserBinding
import com.example.githubuser.viewmodel.MainViewModel

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: UserDetailAdapter
    private lateinit var mainViewModel: MainViewModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        adapter = UserDetailAdapter()

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
        mainViewModel.follow(name, type)
        setData(type)
    }

    private fun recyclerView() {
        adapter.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(context)
        binding.rvUser.adapter = adapter
    }

    fun setData(type: String?) {
        mainViewModel.getState().observe(viewLifecycleOwner, { state->
            if(state) {
                binding.notFound.visibility = View.GONE
                mainViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
                    if (userItems != null) {
                        adapter.setData(userItems)
                        recyclerView()
                        showLoading(false)
                    }
                })
            }
            else {
                binding.notFound.text = "${resources.getString(R.string.not_found)} ${resources.getQuantityString(if(type == "followers") R.plurals.follower else R.plurals.following, 1)}"
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