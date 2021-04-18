package com.example.githubuser.model.favorite

import androidx.lifecycle.LiveData

class FavoriteRepository(private val favDao: FavDao) {
    var listFav: LiveData<List<Favorite>> = favDao.getAll()

    suspend fun insert(fav: Favorite) {
        favDao.insert(fav)
    }

    suspend fun delete(username: String) {
        favDao.delete(username)
    }
}