package com.example.instagramapp.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class Preferences @Inject constructor(private val preferences: SharedPreferences) {
    private val userId: String = "userId"
    private val token: String = "token"

    fun saveToken(value: String) {
        val editor = preferences.edit()
        editor.putString(token, value)
        editor.apply()
    }
    fun saveUserId(value: String) {
        val editor = preferences.edit()
        editor.putString(userId, value)
        editor.apply()
    }

    fun getToken(): String {
        return preferences.getString(token, "") ?: ""
    }

    fun getUserId(): String {
        return preferences.getString(userId, "") ?: ""
    }

    fun deleteToken() {
        val editor = preferences.edit()
        editor.remove(token)
        editor.remove(userId)
        editor.apply()
    }


}