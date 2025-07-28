package com.example.homehub

import com.example.homehub.repositories.MainGateRepository
import com.example.homehub.states.MainGateState
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.json.JSONObject

class MainGateController @Inject constructor(
    private val repository: MainGateRepository
) {

    val status: StateFlow<MainGateState?> = repository.getMainGateStatus()
        .stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, null)

    fun init() {
        repository.init()
    }

    suspend fun activate(): Boolean {
        return repository.activate()
    }

    suspend fun reboot(): Boolean {
        return repository.reboot()
    }

    suspend fun updateConfig(configJson: JSONObject): Boolean {
        return repository.updateConfig(configJson)
    }
}