package com.example.githubuser.other.helper

import android.database.Cursor
import com.example.githubuser.model.favorite.Favorite

object MappingHelper {

    fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<Favorite> {
        val listFav = ArrayList<Favorite>()

        favCursor?.apply {
            while(moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val avatar = getString(getColumnIndexOrThrow("avatar"))
                val username = getString(getColumnIndexOrThrow("username"))
                listFav.add(
                    Favorite(
                        id=id,
                        username=username,
                        avatar=avatar
                    )
                )
            }
        }
        return listFav
    }
}