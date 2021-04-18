package com.example.githubuser.other.alarm

import android.content.Context
import androidx.core.content.edit

internal class MyPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val ALARM = "alarm"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setAlarm(value: Boolean) {
        preferences.edit {
            putBoolean(ALARM, value)
        }
    }

    fun getAlarm(): Boolean = preferences.getBoolean(ALARM, false)
}