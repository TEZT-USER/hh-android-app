package com.example.homehub.states

import com.example.homehub.enums.MainGateStateEnum
import com.example.homehub.utils.extensions.toEnumOrDefault
import org.json.JSONObject

data class MainGateState(
    val state: MainGateStateEnum = MainGateStateEnum.CLOSED,
    val isObstructed: Boolean = false,
) {
    companion object {
        fun fromJson(json: JSONObject): MainGateState {
            return MainGateState(
                state = json.optString("gateState").toEnumOrDefault(MainGateStateEnum.STOPPED),
                isObstructed = json.optBoolean("isObstructed", false)
            )
        }
    }
}