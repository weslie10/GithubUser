package com.example.githubuser.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String? = "",
    var avatar: String? = "",
) : Parcelable
