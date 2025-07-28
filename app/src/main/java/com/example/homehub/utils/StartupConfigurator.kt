package com.example.homehub.utils

import com.example.homehub.constants.Constants
import jakarta.inject.Inject
import java.util.UUID

/**
 * Takes care of the startup configuration.
 */
class StartupConfigurator @Inject constructor(
    private val preferences: Preferences
) {
    fun initialize() {
        // Create user ID if it does not exist
        if (!preferences.existsPreference(Constants.PREFERENCE_USER_ID)) {
            val guid = UUID.randomUUID().toString()
            preferences.setPreference(Constants.PREFERENCE_USER_ID, guid)
        }
    }
}
