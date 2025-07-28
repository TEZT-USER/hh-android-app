package com.example.homehub.utils

import android.util.Log
import com.example.homehub.constants.Constants
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@Singleton
class MQTTManager @Inject constructor(
    private val preferences: Preferences
) {
    private lateinit var mqttClient: MqttAsyncClient
    private val connectCallbacks = mutableMapOf<String, () -> Unit>()
    private val disconnectCallbacks = mutableMapOf<String, (Throwable?) -> Unit>()
    private val topicCallbacks = mutableMapOf<String, (JSONObject) -> Unit>()
    private val connectionDeferred = AtomicReference<CompletableDeferred<Unit>?>(null)
    private val subscribedTopics = mutableSetOf<String>()
    private var reconnectJob: Job? = null
    private val isVisible = AtomicBoolean(false)

    /**
     * Connects to the MQTT broker
     */
    private fun connectMqttBroker(): CompletableDeferred<Unit> {
        // If already connected, return a completed deferred object.
        if (::mqttClient.isInitialized && mqttClient.isConnected) {
            return CompletableDeferred(Unit)
        }

        // If a connection is in progress, return the same deferred object.
        connectionDeferred.get()?.let { return it }

        // Start a new connection attempt.
        val deferred = CompletableDeferred<Unit>()
        connectionDeferred.set(deferred)

        try {
            if (!areMqttParamsSet()) {
                Log.e("MQTTManager", "MQTT parameters are not set. Check your configuration.")
                deferred.completeExceptionally(Exception("MQTT parameters are not set"))
                connectionDeferred.set(null)
                return deferred
            }

            val mqttHost =
                "${preferences.getPreference<String>(Constants.PREFERENCE_MQTT_HOST, "")}:${
                    preferences.getPreference<String>(Constants.PREFERENCE_MQTT_PORT, "")
                }"
            mqttClient = MqttAsyncClient(mqttHost, MqttClient.generateClientId(), null)

            val options = MqttConnectOptions().apply {
                userName = preferences.getPreference<String>(Constants.PREFERENCE_MQTT_USER, "")
                password =
                    preferences.getPreference<String>(Constants.PREFERENCE_MQTT_PASS, "")
                        .toCharArray()
            }

            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(Constants.LOG_TAG_MQTT, "Connected successfully")
                    setMqttCallbacks()
                    deferred.complete(Unit)
                    connectionDeferred.set(null) // Clear the deferred once connection succeeds.

                    for (callback in connectCallbacks) {
                        callback.value()
                    }
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(Constants.LOG_TAG_MQTT, "Failed to connect", exception)
                    deferred.completeExceptionally(
                        exception ?: Exception("Unknown connection error")
                    )
                    connectionDeferred.set(null) // Clear the deferred on failure.
                }
            })
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG_MQTT, "Error connecting to MQTT broker", e)
            deferred.completeExceptionally(e)
            connectionDeferred.set(null)
        }

        return deferred
    }

    /**
     * Sets up MQTT callbacks
     */
    private fun setMqttCallbacks() {
        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(
                    Constants.LOG_TAG_MQTT,
                    "Received message on topic: $topic, message: ${message.toString()}"
                )
                if (topic != null && message != null) {
                    val json = JSONObject(message.payload.toString(Charsets.UTF_8))
                    topicCallbacks[topic]?.invoke(json)
                }
            }

            override fun connectionLost(cause: Throwable?) {
                Log.e(Constants.LOG_TAG_MQTT, "Connection lost, reconnecting..", cause)
                for (callback in disconnectCallbacks) {
                    callback.value(cause)
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(
                    Constants.LOG_TAG_MQTT,
                    "Delivery complete for message with token: ${token?.message}"
                )
            }
        })
    }

    /**
     * Disconnects from the MQTT broker
     */
    fun disconnectMqttBroker() {
        if (::mqttClient.isInitialized && mqttClient.isConnected) {
            try {
                mqttClient.disconnect(null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(Constants.LOG_TAG_MQTT, "Disconnected successfully")
                        for (callback in disconnectCallbacks) {
                            callback.value(null)
                        }
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.e(Constants.LOG_TAG_MQTT, "Failed to disconnect", exception)
                        for (callback in disconnectCallbacks) {
                            callback.value(exception)
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e(Constants.LOG_TAG_MQTT, "Error disconnecting from MQTT broker", e)
                for (callback in disconnectCallbacks) {
                    callback.value(e)
                }
            }
        }
    }

    /**
     * Returns if MQTT parameters are set
     */
    fun areMqttParamsSet(): Boolean {
        val mqttHost = preferences.getPreference<String>(Constants.PREFERENCE_MQTT_HOST, "")
        val mqttPort = preferences.getPreference<String>(Constants.PREFERENCE_MQTT_PORT, "")
        val mqttUser = preferences.getPreference<String>(Constants.PREFERENCE_MQTT_USER, "")
        val mqttPass = preferences.getPreference<String>(Constants.PREFERENCE_MQTT_PASS, "")

        return mqttHost != "" && mqttPort != "" && mqttUser != "" && mqttPass != ""
    }

    /**
     * Publishes a message to a topic
     */
    fun publishMessage(topic: String, json: JSONObject, qos: Int = 2, isRetained: Boolean = false) {
        try {
            runBlocking {
                // Ensure the connection is established before publishing.
                connectMqttBroker().await()

                if (mqttClient.isConnected) {
                    val message = MqttMessage(json.toString().toByteArray())
                    message.qos = qos
                    message.isRetained = isRetained

                    mqttClient.publish(topic, message, null, object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            Log.d(Constants.LOG_TAG_MQTT, "Published message to topic: $topic")
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            Log.e(Constants.LOG_TAG_MQTT, "Failed to publish message", exception)
                        }
                    })
                } else {
                    Log.e(Constants.LOG_TAG_MQTT, "MQTT client is not connected")
                }
            }
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG_MQTT, "Error publishing message", e)
        }
    }

    /**
     * Subscribes to a topic
     */
    fun subscribeToTopic(topic: String, callback: ((JSONObject) -> Unit)? = null) {
        try {
            if (subscribedTopics.contains(topic)) {
                Log.d(Constants.LOG_TAG_MQTT, "Already subscribed to topic: $topic, skipping.")
                return
            }
            runBlocking {
                connectMqttBroker().await()
                if (mqttClient.isConnected) {
                    mqttClient.subscribe(topic, 2, null, object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            Log.d(Constants.LOG_TAG_MQTT, "Subscribed to topic: $topic")
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            Log.e(Constants.LOG_TAG_MQTT, "Failed to subscribe to topic", exception)
                        }
                    })
                } else {
                    Log.e(Constants.LOG_TAG_MQTT, "MQTT client is not connected")
                }
                callback?.let { topicCallbacks[topic] = it }
                subscribedTopics.add(topic)
            }
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG_MQTT, "Error subscribing to topic", e)
        }
    }

    /**
     * Call this when app or tile becomes visible
     */
    fun setVisible() {
        isVisible.set(true)
        startReconnectLoop()
    }

    /**
     * Call this when app or tile is closed/invisible
     */
    fun setInvisible() {
        isVisible.set(false)
        stopReconnectLoop()
        disconnectMqttBroker()
    }

    private fun startReconnectLoop() {
        if (reconnectJob?.isActive == true) return
        reconnectJob = CoroutineScope(Dispatchers.IO).launch {
            var delayMs = 2000L
            val maxDelay = 60000L
            while (isVisible.get()) {
                try {
                    connectMqttBroker().await()
                    // Resubscribe to topics after (re)connect
                    for (topic in subscribedTopics) {
                        subscribeToTopic(topic)
                    }
                    break // Connected, stop loop
                } catch (e: Exception) {
                    // Exponential backoff
                    delay(delayMs)
                    delayMs = (delayMs * 2).coerceAtMost(maxDelay)
                }
            }
        }
    }

    private fun stopReconnectLoop() {
        reconnectJob?.cancel()
        reconnectJob = null
    }

    /**
     * Adds a connected callback
     */
    fun addConnectedCallback(id: String, callback: () -> Unit) {
        if (!connectCallbacks.containsKey(id)) {
            connectCallbacks[id] = callback
        }
    }

    /**
     * Adds a disconnected callback
     */
    fun addDisconnectedCallback(id: String, callback: (Throwable?) -> Unit) {
        if (!disconnectCallbacks.containsKey(id)) {
            disconnectCallbacks[id] = callback
        }
    }
}
