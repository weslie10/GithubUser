package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.githubuser.BuildConfig
import com.example.githubuser.model.repository.Repository
import com.example.githubuser.model.user.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()
    val state = MutableLiveData<Boolean>()
    val search = MutableLiveData<String>()
    val repo = MutableLiveData<ArrayList<Repository>>()

    companion object {
        const val URL = "https://api.github.com"
        val TAG = MainViewModel::class.java.simpleName
    }

    fun getUser(): LiveData<ArrayList<User>> = listUser
    fun getState(): LiveData<Boolean> = state
    fun getSearch(): LiveData<String> = search
    fun getRepo(): LiveData<ArrayList<Repository>> = repo

    fun setSearch(text: String) {
        search.postValue(text)
    }

    fun cancelRequest(tag: String) {
        AndroidNetworking.forceCancel(tag)
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
                        for (i in 0 until response.length()) {
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
                    Log.d(TAG, "all()")
                    if (error.errorCode != 0) {
                        Log.d(TAG, "onError errorCode : " + error.errorCode)
                        Log.d(TAG, "onError errorBody : " + error.errorBody)
                        Log.d(TAG, "onError errorDetail : " + error.errorDetail)
                    } else {
                        Log.d(TAG, "onError errorDetail : " + error.errorDetail)
                    }
                }
            })
    }

    fun findByUsername(name: String?) {
        val listData = arrayListOf<User>()
        AndroidNetworking.get("$URL/search/users?q=${name}")
            .addHeaders("Authorization", BuildConfig.GITHUB_TOKEN)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.LOW)
            .setTag("req")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val count = response.getInt("total_count")
                        if (count > 0) {
                            val data = response.getJSONArray("items")
                            for (i in 0 until data.length()) {
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
                        } else {
                            state.postValue(false)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(error: ANError) {
                    if (error.errorCode != 0) {
                        Log.d(TAG, "onError errorCode : " + error.errorCode)
                        Log.d(TAG, "onError errorBody : " + error.errorBody)
                        Log.d(TAG, "onError errorDetail : " + error.errorDetail)
                    } else {
                        Log.d(TAG, "onError errorDetail : " + error.errorDetail)
                    }
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
                        if (response.length() > 0) {
                            for (i in 0 until response.length()) {
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
                    if (error.errorCode != 0) {
                        Log.d(TAG, "onError errorCode : " + error.errorCode)
                        Log.d(TAG, "onError errorBody : " + error.errorBody)
                        Log.d(TAG, "onError errorDetail : " + error.errorDetail)
                    } else {
                        Log.d(TAG, "onError errorDetail : " + error.errorDetail)
                    }
                }
            })
    }

    fun repo(name: String?) {
        val listData: ArrayList<Repository> = arrayListOf()
        AndroidNetworking.get("${URL}/users/${name}/repos")
            .addHeaders("Authentication", BuildConfig.GITHUB_TOKEN)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        if (response.length() > 0) {
                            for (i in 0 until response.length()) {
                                with(response.getJSONObject(i)) {
                                    listData.add(
                                        Repository(
                                            name = getString("name"),
                                            description = getString("description"),
                                            url = getString("html_url")
                                        )
                                    )
                                }
                            }
                            repo.postValue(listData)
                            state.postValue(true)
                        } else {
                            state.postValue(false)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(error: ANError) {
                    if (error.errorCode != 0) {
                        // received error from server
                        // error.getErrorCode() - the error code from server
                        // error.getErrorBody() - the error body from server
                        // error.getErrorDetail() - just an error detail
                        Log.d(TAG, "onError errorCode : " + error.errorCode)
                        Log.d(TAG, "onError errorBody : " + error.errorBody)
                        Log.d(TAG, "onError errorDetail : " + error.errorDetail)
                    } else {
                        // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                        Log.d(TAG, "onError errorDetail : " + error.errorDetail)
                    }
                }
            })
    }
}