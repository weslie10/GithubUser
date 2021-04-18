package com.example.githubuser.model.favorite

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite")
    fun getCursor(): Cursor

    @Query("SELECT * FROM favorite where username=:username")
    fun getByUsername(username: String): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE username=:username")
    suspend fun delete(username: String)
}