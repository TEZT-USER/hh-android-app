package com.example.homehub.services

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.example.homehub.R
import com.example.homehub.enums.toUiText
import com.example.homehub.repositories.MainGate
import com.example.homehub.utils.MQTTManager
import kotlinx.coroutines.*

class MainGateTileService : TileService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var isOperationInProgress = false

    /**
     * Called when the tile is added to Quick Settings
     */
    override fun onTileAdded() {
        qsTile.label = applicationContext.getString(R.string.mainGate_quickSettingsTileName)
        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_tile_home_hub_light)
        qsTile.updateTile()
    }

    /**
     * Called when the tile becomes visible
     */
    override fun onStartListening() {
        qsTile.subtitle = ""
        qsTile.label = applicationContext.getString(R.string.mainGate_quickSettingsTileName)
        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_tile_home_hub_light)

        try {
            MainGate.init(applicationContext)
            MQTTManager.setVisible(applicationContext)

            if (MQTTManager.areMqttParamsSet(applicationContext)) {
                qsTile.state = Tile.STATE_INACTIVE
            } else {
                qsTile.state = Tile.STATE_UNAVAILABLE
            }

            qsTile.subtitle = MainGate.status.value?.state?.toUiText(applicationContext)
        } catch (e: Exception) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            android.util.Log.e("MainGateTileService", "Error initializing MQTT or MainGate", e)
        }

        qsTile.updateTile()
    }

    override fun onStopListening() {
        MQTTManager.setInvisible()
    }

    /**
     * Called when the tile is clicked
     */
    override fun onClick() {
        super.onClick()

        if (isOperationInProgress) {
            return
        }
        isOperationInProgress = true

        val startTime = System.currentTimeMillis()

        serviceScope.launch {
            var operationSuccessful = true
            var operationFinished = false

            launch(Dispatchers.IO) {
                operationSuccessful = MainGate.activateGate(applicationContext)
                operationFinished = true
            }

            launch(Dispatchers.Main) {
                val loadingIcons = arrayOf(
                    R.drawable.ic_tile_loading_1,
                    R.drawable.ic_tile_loading_2,
                    R.drawable.ic_tile_loading_3
                )
                var frameNumber = 1
                while (System.currentTimeMillis() - startTime < 2000 || !operationFinished) {
                    qsTile.icon =
                        Icon.createWithResource(applicationContext, loadingIcons[frameNumber - 1])
                    qsTile.updateTile()
                    delay(250)
                    frameNumber = (frameNumber % 3) + 1

                    if (System.currentTimeMillis() - startTime > 6000) {
                        operationSuccessful = false
                        break
                    }
                }

                if (operationSuccessful) {
                    qsTile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_success)
                } else {
                    qsTile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_error)
                }
                qsTile.updateTile()
                delay(1000)

                qsTile.label = applicationContext.getString(R.string.mainGate_quickSettingsTileName)
                qsTile.icon =
                    Icon.createWithResource(applicationContext, R.drawable.ic_tile_home_hub_light)
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.subtitle = MainGate.status.value?.state?.toUiText(applicationContext)
                qsTile.updateTile()

                isOperationInProgress = false
            }
        }
    }
}
