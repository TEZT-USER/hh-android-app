package com.example.homehub.viewmodels

import androidx.lifecycle.ViewModel
import com.example.homehub.MainGateController
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject

@HiltViewModel
class ConfigScreenViewModel @Inject constructor(
    private val mainGateController: MainGateController
) : ViewModel() {
    fun updateConfig(configJson: JSONObject): Flow<Boolean> = flow {
        emit(mainGateController.updateConfig(configJson))
    }.flowOn(Dispatchers.IO)
}

