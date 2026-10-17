package com.animations.utils.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefs(
    context: Context
) {
    private var _prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun put(key: String, value: String) {
        _prefs.edit { putString(key, value) }
    }

    fun get(key: String, defaultValue: String = ""): String? {
        return _prefs.getString(key, defaultValue)
    }

    companion object {
        const val PREF_NAME = "SharedPrefs"
    }
}
