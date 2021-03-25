package com.example.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.githubuser.BuildConfig
import com.example.githubuser.model.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainViewModel: ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()
    val state = MutableLiveData<Boolean>()
    val search = MutableLiveData<String>()

    companion object {
        const val URL = "https://api.github.com"
    }

    fun getUser(): LiveData<ArrayList<User>> = listUser
    fun getState(): LiveData<Boolean> = state
    fun getSearch(): LiveData<String> = search

    fun setSearch(text: String) {
        search.postValue(text)
    }

    fun all() {
        val listData: ArrayList<User> = arrayListOf()
        AndroidNetworking.get("$URL/users")
            .addHeaders("Authorization", BuildConfig.GITHUB_TOKEN)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        for (i in 0 until response.length() - 1) {
                            with(response.getJSONObject(i)) {
                                listData.add(
                                    User(
                                        username = getString("login"),
                                        avatar = getString("avatar_url")
                                    )
                                )
                            }
                        }
                        listUser.postValue(listData)
                        state.postValue(true)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                override fun onError(error: ANError) {
                    error.printStackTrace()
                }
            })
    }

    fun findByUsername(name: String?) {
        val listData = arrayListOf<User>()
        AndroidNetworking.get("$URL/search/users?q=${name}")
            .addHeaders("Authorization", BuildConfig.GITHUB_TOKEN)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val count = response.getInt("total_count")
                        if(count > 0) {
                            val data = response.getJSONArray("items")
                            for (i in 0 until data.length() - 1) {
                                with(data.getJSONObject(i)) {
                                    listData.add(
                                        User(
                                            username = getString("login"),
                                            avatar = getString("avatar_url")
                                        )
                                    )
                                }
                            }
                            listUser.postValue(listData)
                            state.postValue(true)
                        }
                        else {
                            state.postValue(false)
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

    fun follow(name: String?, type: String?) {
        val listData: ArrayList<User> = arrayListOf()
        AndroidNetworking.get("${URL}/users/${name}/${type}")
            .addHeaders("Authentication", BuildConfig.GITHUB_TOKEN)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        if(response.length() > 0) {
                            for (i in 0 until response.length() - 1) {
                                with(response.getJSONObject(i)) {
                                    listData.add(
                                        User(
                                            username = getString("login"),
                                            avatar = getString("avatar_url")
                                        )
                                    )
                                }
                            }
                            listUser.postValue(listData)
                            state.postValue(true)
                        } else {
                            state.postValue(false)
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
}