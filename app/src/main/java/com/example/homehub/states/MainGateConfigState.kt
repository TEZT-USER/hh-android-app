package com.example.homehub.states

import org.json.JSONObject

data class MainGateConfig(
    val delayedClose: MainGateConfigDelayedClose = MainGateConfigDelayedClose(),
    val unobstructedClose: MainGateConfigUnobstructedClose = MainGateConfigUnobstructedClose(),
    val nighttimeClose: MainGateConfigNighttimeClose = MainGateConfigNighttimeClose(),
) {
    companion object {
        fun fromJson(json: JSONObject): MainGateConfig {
            return MainGateConfig(
                delayedClose = json.optJSONObject("delayedClose")?.let {
                    MainGateConfigDelayedClose.fromJson(it)
                } ?: MainGateConfigDelayedClose(),
                unobstructedClose = json.optJSONObject("unobstructedClose")?.let {
                    MainGateConfigUnobstructedClose.fromJson(it)
                } ?: MainGateConfigUnobstructedClose(),
                nighttimeClose = json.optJSONObject("nighttimeClose")?.let {
                    MainGateConfigNighttimeClose.fromJson(it)
                } ?: MainGateConfigNighttimeClose()
            )
        }
    }
}

data class MainGateConfigDelayedClose(
    val enabled: Boolean = false,
    val timeUntilCloseSeconds: Int = 120,
) {
    companion object {
        fun fromJson(json: JSONObject): MainGateConfigDelayedClose {
            return MainGateConfigDelayedClose(
                enabled = json.optBoolean("enabled", false),
                timeUntilCloseSeconds = json.optInt("timeUntilCloseSeconds", 120)
            )
        }
    }
}

data class MainGateConfigUnobstructedClose(
    val enabled: Boolean = false,
    val timeUntilCloseSeconds: Int = 30,
) {
    companion object {
        fun fromJson(json: JSONObject): MainGateConfigUnobstructedClose {
            return MainGateConfigUnobstructedClose(
                enabled = json.optBoolean("enabled", false),
                timeUntilCloseSeconds = json.optInt("timeUntilCloseSeconds", 30)
            )
        }
    }
}

data class MainGateConfigNighttimeClose(
    val enabled: Boolean = false,
    val startSeconds: Int = 82800,
    val endSeconds: Int = 18000,
    val timeUntilCloseSeconds: Int = 1800,
) {
    companion object {
        fun fromJson(json: JSONObject): MainGateConfigNighttimeClose {
            return MainGateConfigNighttimeClose(
                enabled = json.optBoolean("enabled", false),
                startSeconds = json.optInt("startSeconds", 82800),
                endSeconds = json.optInt("endSeconds", 18000),
                timeUntilCloseSeconds = json.optInt("timeUntilCloseSeconds", 1800)
            )
        }
    }
}
