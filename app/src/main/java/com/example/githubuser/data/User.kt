package com.example.githubuser.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String?,
    var username: String?,
    var avatar: Int?,
    var follower: Int?,
    var following: Int?,
    var repository: Int?,
    var company: String?,
    var location: String?,
):Parcelable