package com.example.githubuser.other.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuser.model.favorite.FavDao
import com.example.githubuser.model.favorite.FavoriteDatabase
import com.example.githubuser.view.activity.FavoriteActivity.Companion.AUTHORITY
import com.example.githubuser.view.activity.FavoriteActivity.Companion.CONTENT_URI
import com.example.githubuser.view.activity.FavoriteActivity.Companion.TABLE_NAME

class FavoriteProvider : ContentProvider() {

    private lateinit var favDao: FavDao
    companion object {
        private const val FAVORITE = 1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
        }
    }

    override fun onCreate(): Boolean {
        favDao = FavoriteDatabase.getInstance(context as Context).favDao()
        return false
    }
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> favDao.getCursor()
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return null
    }
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return 1
    }
}