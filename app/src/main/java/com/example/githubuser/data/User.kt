package com.example.githubuser.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String?,
    var username: String?,
    var photo: Int?,
    var follower: Int?,
    var following: Int?,
    var company: String?,
    var location: String?,
    var repository: Array<String>?
):Parcelable