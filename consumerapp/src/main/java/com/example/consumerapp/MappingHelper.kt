package com.example.consumerapp

import android.database.Cursor

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