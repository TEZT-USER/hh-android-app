package com.example.homehub.viewmodels

import androidx.lifecycle.ViewModel
import com.example.homehub.MainGateController
import com.example.homehub.utils.MQTTManager
import com.example.homehub.utils.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val mqttManager: MQTTManager,
    private val mainGateController: MainGateController,
    private val preferences: Preferences,
) : ViewModel() {
    val isMqttConfigured: Boolean = mqttManager.areMqttParamsSet()

    val mainGateStatus = mainGateController.status

    fun getStringPreference(key: String, default: String = ""): String =
        preferences.getPreference(key, default)

    fun setStringPreference(key: String, value: String) =
        preferences.setPreference(key, value)

}