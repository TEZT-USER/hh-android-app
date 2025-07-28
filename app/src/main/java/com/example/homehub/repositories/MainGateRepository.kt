package com.example.homehub.repositories

import android.content.Context
import android.util.Log
import com.example.homehub.constants.Constants
import com.example.homehub.enums.MainGateStateEnum
import com.example.homehub.states.MainGateConfig
import com.example.homehub.states.MainGateState
import com.example.homehub.utils.MQTTManager
import com.example.homehub.utils.Preferences
import jakarta.inject.Inject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

class MainGateRepository @Inject constructor(
    private val mqttManager: MQTTManager,
    private val prefs: Preferences
) {
    private var statusReadyDeferred = CompletableDeferred<Unit>()

    private val _status = MutableStateFlow<MainGateState?>(null)
    val status: StateFlow<MainGateState?> get() = _status

    var config: MainGateConfig? = null

    /**
     * Init function - subscribes to MQTT topics in advance
     */
    fun init(context: Context) {
        MQTTManager.subscribeToTopic(
            context,
            Constants.MQTT_TOPIC_MAIN_GATE_STATUS_GATE
        ) { messageJson ->
            _status.value = MainGateState.fromJson(messageJson)

            if (!statusReadyDeferred.isCompleted) {
                statusReadyDeferred.complete(Unit)
            }
        }
        MQTTManager.subscribeToTopic(
            context,
            Constants.MQTT_TOPIC_MAIN_GATE_CONFIG
        ) { messageJson ->
            config = MainGateConfig.fromJson(messageJson)
        }
        MQTTManager.addConnectedCallback("MainGate.init") {
            if (!statusReadyDeferred.isCompleted) {
                statusReadyDeferred.cancel()
            }
            statusReadyDeferred = CompletableDeferred()
            _status.value = null
            config = null
        }
        MQTTManager.addDisconnectedCallback("MainGate.init") {
            if (!statusReadyDeferred.isCompleted) {
                statusReadyDeferred.cancel()
            }
            statusReadyDeferred = CompletableDeferred()
            _status.value = null
            config = null
        }
    }

    /**
     * Opens/closes main gate (based on the current state)
     */
    suspend fun activate(context: Context): Boolean {
        // Ensure MQTT is initialized without unnecessary disconnection
        if (!MQTTManager.areMqttParamsSet(context)) {
            Log.e("MainGate", "MQTT parameters are not set. Cannot activate gate.")
            return false
        }

        // Reinitialize subscriptions if needed
        init(context)

        try {
            // Wait for status to arrive
            if (_status.value == null && !statusReadyDeferred.isCompleted) {
                statusReadyDeferred.await()
            }

            val initiatorId = Preferences.getPreference(context, Constants.PREFERENCE_USER_ID, "")
            val jsonCommand = JSONObject().put("initiatorId", initiatorId)

            val topic = if (_status.value?.state == MainGateStateEnum.CLOSED) {
                Constants.MQTT_TOPIC_MAIN_GATE_OPEN
            } else {
                Constants.MQTT_TOPIC_MAIN_GATE_CLOSE
            }

            MQTTManager.publishMessage(context, topic, jsonCommand)
            return true
        } catch (e: Exception) {
            Log.e("MainGate", "Error activating gate", e)
            return false
        }
    }

    /**
     * Updates main gate configuration
     */
    fun updateConfig(context: Context, configJson: JSONObject): Boolean {
        return try {
            MQTTManager.publishMessage(
                context,
                Constants.MQTT_TOPIC_MAIN_GATE_CONFIG,
                configJson,
                1,
                true
            )
            true
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG_MAIN_GATE, "Failed to update config", e)
            false
        }
    }

    /**
     * Reboots main gate
     */
    fun reboot(context: Context): Boolean {
        return try {
            MQTTManager.publishMessage(
                context,
                Constants.MQTT_TOPIC_MAIN_GATE_REBOOT,
                JSONObject(),
                2,
                false
            )
            true
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG_MAIN_GATE, "Failed to reboot", e)
            false
        }
    }
}
