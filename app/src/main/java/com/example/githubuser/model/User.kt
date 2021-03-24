package com.example.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String? = "",
    var username: String? = "",
    var avatar: String? = "",
    var repository: Int? = 0,
    var follower: Int? = 0,
    var following: Int? = 0,
    var location: String? = "",
    var company: String? = ""
) : Parcelable
