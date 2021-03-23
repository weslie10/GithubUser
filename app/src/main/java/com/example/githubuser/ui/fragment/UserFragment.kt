package com.example.githubuser.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.githubuser.MainActivity.Companion.TOKEN
import com.example.githubuser.MainActivity.Companion.URL
import com.example.githubuser.R
import com.example.githubuser.adapter.UserDetailAdapter
import com.example.githubuser.data.User
import com.example.githubuser.databinding.FragmentUserBinding
import org.json.JSONArray
import org.json.JSONException

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: UserDetailAdapter

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)
        adapter = UserDetailAdapter()
        recyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString(NAME)
        val type = arguments?.getString(TYPE)
        getAll(name, type)
    }

    private fun recyclerView() {
        showLoading(false)
        adapter.notifyDataSetChanged()
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(context)
        binding.rvUser.adapter = adapter
    }

    fun getAll(name: String?, type: String?) {
        showLoading(true)
        var listData: ArrayList<User> = arrayListOf()
        AndroidNetworking.get("${URL}/users/${name}/${type}")
            .addHeaders("Authentication", TOKEN)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        if(response.length() > 0) {
                            binding.notFound.visibility = View.GONE
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
                            recyclerView()
                        }
                        else {
                            binding.notFound.text = "${resources.getString(R.string.not_found)} ${resources.getQuantityString(if(type == "followers") R.plurals.follower else R.plurals.following, 2)}"
                            binding.notFound.visibility = View.VISIBLE
                            showLoading(false)
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