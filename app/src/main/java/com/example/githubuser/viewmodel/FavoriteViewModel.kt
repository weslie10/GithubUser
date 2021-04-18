package com.example.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.githubuser.model.favorite.Favorite
import com.example.githubuser.model.favorite.FavoriteDatabase
import com.example.githubuser.model.favorite.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    var listFav: LiveData<List<Favorite>>
    private var repo: FavoriteRepository

    init {
        val favDao = FavoriteDatabase.getInstance(application).favDao()
        repo = FavoriteRepository(favDao)
        listFav = repo.listFav
    }

    fun addFavorite(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(favorite)
        }
    }

    fun deleteFavorite(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(username)
        }
    }
}