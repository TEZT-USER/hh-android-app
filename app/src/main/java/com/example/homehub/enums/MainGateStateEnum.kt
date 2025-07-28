package com.example.homehub.enums

import android.content.Context
import com.example.homehub.R

enum class MainGateStateEnum {
    OPEN, CLOSED, OPENING, CLOSING, STOPPED
}

fun MainGateStateEnum.toUiText(context: Context): String {
    return when (this) {
        MainGateStateEnum.OPEN -> context.getString(R.string.mainGate_state_open)
        MainGateStateEnum.CLOSED -> context.getString(R.string.mainGate_state_closed)
        MainGateStateEnum.OPENING -> context.getString(R.string.mainGate_state_opening)
        MainGateStateEnum.CLOSING -> context.getString(R.string.mainGate_state_closing)
        MainGateStateEnum.STOPPED -> context.getString(R.string.mainGate_state_stopped)
    }
}