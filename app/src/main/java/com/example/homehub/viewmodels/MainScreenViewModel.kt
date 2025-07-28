package com.example.homehub.viewmodels

import androidx.lifecycle.ViewModel
import com.example.homehub.MainGateController
import com.example.homehub.utils.MQTTManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@HiltViewModel
open class MainScreenViewModel @Inject constructor(
    private val mainGateController: MainGateController,
    mqttManager: MQTTManager
) : ViewModel() {
    val isFormEnabled: Boolean = mqttManager.areMqttParamsSet()
    val mainGateStatus = mainGateController.status

    fun activateMainGate(): Flow<Boolean> = flow {
        emit(mainGateController.activate())
    }.flowOn(Dispatchers.IO)
}