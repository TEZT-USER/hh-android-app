package com.example.homehub.viewmodels

import androidx.lifecycle.ViewModel
import com.example.homehub.utils.MQTTManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val mqttManager: MQTTManager
) : ViewModel() {
    val isMqttConfigured: Boolean = mqttManager.areMqttParamsSet()

    val mainGateStatus = mainGateController.status

}

