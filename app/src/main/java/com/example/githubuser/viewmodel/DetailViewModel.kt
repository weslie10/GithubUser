package com.example.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.githubuser.BuildConfig
import com.example.githubuser.model.User
import org.json.JSONException
import org.json.JSONObject

class DetailViewModel: ViewModel() {
    val user = MutableLiveData<User>()

    fun getUser(): LiveData<User> = user

    fun getDetail(name: String) {
        AndroidNetworking.get("${MainViewModel.URL}/users/${name}")
            .addHeaders("Authorization", BuildConfig.GITHUB_TOKEN)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        with(response) {
                            user.postValue(
                                User(
                                    username = getString("login"),
                                    name = getString("name"),
                                    avatar = getString("avatar_url"),
                                    repository = getInt("public_repos"),
                                    follower = getInt("followers"),
                                    following = getInt("following"),
                                    company = getString("company"),
                                    location = getString("location")
                                )
                            )
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