package com.example.fefusport.data.preferences

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setCurrentUserId(userId: Long) {
        prefs.edit { putLong(KEY_CURRENT_USER, userId) }
    }

    fun getCurrentUserId(): Long? {
        val stored = prefs.getLong(KEY_CURRENT_USER, DEFAULT_USER_ID)
        return if (stored == DEFAULT_USER_ID) null else stored
    }

    fun clearSession() {
        prefs.edit { remove(KEY_CURRENT_USER) }
    }

    companion object {
        private const val PREFS_NAME = "fefusport_session"
        private const val KEY_CURRENT_USER = "current_user_id"
        private const val DEFAULT_USER_ID = -1L
    }
}

