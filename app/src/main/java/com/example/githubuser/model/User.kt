package com.example.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String? = "",
    val username: String? = "",
    val avatar: String? = "",
    val repository: Int? = 0,
    val follower: Int? = 0,
    val following: Int? = 0,
    val location: String? = "",
    val company: String? = ""
) : Parcelable
