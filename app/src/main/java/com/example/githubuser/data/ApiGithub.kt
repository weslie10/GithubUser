package com.example.githubuser.data

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException


class ApiGithub: AppCompatActivity() {
    var listUser: ArrayList<User> = arrayListOf<User>()
    companion object {
        const val URL = "https://api.github.com"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidNetworking.initialize(getApplicationContext());
        val okHttpClient = OkHttpClient().newBuilder().build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)
    }

//    fun getUser(name: String): User {
//        var user = User()
//        AndroidNetworking.get("${URL}/users/${name}")
////            .addHeaders("token", "1234")
//            .setPriority(Priority.HIGH)
//            .build()
//            .getAsJSONArray(object : JSONArrayRequestListener {
//                override fun onResponse(response: JSONArray) {
//                    with(response.getJSONObject(0)) {
//                        user = User(
//                            name = getString("name"),
//                            username = getString("login"),
//                            avatar = getString("avatar_url"),
//                            follower = getInt("followers"),
//                            following = getInt("following"),
//                            repository = getInt("public_repos"),
//                            company = getString("company"),
//                            location = getString("location")
//                        )
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    Log.e("ApiGithub",error.errorBody)
//                    Log.e("ApiGithub",error.errorDetail)
//                }
//            })
//
//        return user
//    }
    
    fun getFollow(name: String, type: String) : ArrayList<User> {
        var listFollow = arrayListOf<User>()
        AndroidNetworking.get("${URL}/users/${name}/${type}")
//            .addHeaders("token", "1234")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    for(i in 0 .. (response.length() - 1)) {
                        with(response.getJSONObject(i)) {
                            var user = User()
                            user.username = getString("login")
                            user.avatar = getString("avatar_url")
                            listFollow.add(user)
                        }
                    }
                }

                override fun onError(error: ANError) {
                    Log.e("ApiGithub",error.errorBody)
                    Log.e("ApiGithub",error.errorDetail)
                }
            })

        return listFollow
    }
}