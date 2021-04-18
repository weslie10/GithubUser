package com.example.githubuser.other.stackwidget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.githubuser.R
import com.example.githubuser.model.favorite.FavDao
import com.example.githubuser.model.favorite.Favorite
import com.example.githubuser.model.favorite.FavoriteDatabase
import com.example.githubuser.other.helper.MappingHelper
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

internal class StackRemoveViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private val listAvatar = ArrayList<Bitmap>()
    private val listUsername = ArrayList<String>()
    private val favDao: FavDao = FavoriteDatabase.getInstance(context).favDao()

    override fun onCreate() { }

    override fun onDestroy() { }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()

        val cursor: Cursor = favDao.getCursor()
        val list: List<Favorite> = MappingHelper.mapCursorToArrayList(cursor)
        for (item in list) {
            val url = URL(item.avatar)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)

            listAvatar.add(myBitmap)
            listUsername.add(item.username)
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, listAvatar[position])
        rv.setTextViewText(R.id.textView, listUsername[position])

        val extras = bundleOf(
            ImageStackWidget.EXTRA_ITEM to position,
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getCount(): Int = listAvatar.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}