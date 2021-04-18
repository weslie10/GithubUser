package com.example.githubuser.model.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name="username") val username: String,
    @ColumnInfo(name="avatar") val avatar: String
)