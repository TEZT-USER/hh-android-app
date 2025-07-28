package com.example.homehub.utils

import android.content.Context
import androidx.core.content.edit
import com.example.homehub.constants.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class Preferences @Inject constructor(
    @ApplicationContext val context: Context
) {
    inline fun <reified T> getPreference(key: String, defaultValue: T): T {
        val sharedPreferences =
            context.getSharedPreferences(Constants.PREFERENCES_STORE_NAME, Context.MODE_PRIVATE)
        return when (T::class) {
            String::class -> sharedPreferences.getString(key, defaultValue as? String) as T
            Boolean::class -> sharedPreferences.getBoolean(key, defaultValue as Boolean) as T
            Int::class -> sharedPreferences.getInt(key, defaultValue as Int) as T
            Float::class -> sharedPreferences.getFloat(key, defaultValue as Float) as T
            Long::class -> sharedPreferences.getLong(key, defaultValue as Long) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    inline fun <reified T> setPreference(key: String, value: T) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.PREFERENCES_STORE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            when (T::class) {
                String::class -> putString(key, value as String)
                Boolean::class -> putBoolean(key, value as Boolean)
                Int::class -> putInt(key, value as Int)
                Float::class -> putFloat(key, value as Float)
                Long::class -> putLong(key, value as Long)
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    fun existsPreference(key: String): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(Constants.PREFERENCES_STORE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.contains(key)
    }
}