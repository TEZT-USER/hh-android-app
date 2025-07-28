package com.example.homehub.screens.config_screen

import BasicButton
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.homehub.R
import com.example.homehub.composables.BasicDivider
import com.example.homehub.composables.BasicTitle
import com.example.homehub.composables.BasicToggleSwitch
import com.example.homehub.constants.Constants
import com.example.homehub.ui.theme.HomeHubTheme
import com.example.homehub.utils.Preferences
import com.example.homehub.utils.Toasts

@Composable
fun ScheduleScreen() {
    val context = LocalContext.current
    val preferences = remember { Preferences(context) }
    var isEditMode by remember { mutableStateOf(false) }

    var isDelayedAutocloseToggled by remember {
        mutableStateOf(
            preferences.getPreference(
                Constants.PREFERENCE_MAIN_GATE_CONFIG_DELAYED_AUTOCLOSE_ENABLED,
                Constants.PREFERENCE_MAIN_GATE_CONFIG_DELAYED_AUTOCLOSE_ENABLED_DEFAULT
            )
        )
    }
    var isCrossedAutocloseToggled by remember {
        mutableStateOf(
            preferences.getPreference(
                Constants.PREFERENCE_MAIN_GATE_CONFIG_UNOBSTRUCTED_AUTOCLOSE_ENABLED,
                Constants.PREFERENCE_MAIN_GATE_CONFIG_UNOBSTRUCTED_AUTOCLOSE_ENABLED_DEFAULT
            )
        )
    }
    var delayedAutocloseSeconds by remember {
        mutableIntStateOf(
            preferences.getPreference(
                Constants.PREFERENCE_MAIN_GATE_CONFIG_DELAYED_AUTOCLOSE_SECONDS,
                Constants.PREFERENCE_MAIN_GATE_CONFIG_DELAYED_AUTOCLOSE_SECONDS_DEFAULT
            )
        )
    }
    var crossedAutocloseSeconds by remember {
        mutableIntStateOf(
            preferences.getPreference(
                Constants.PREFERENCE_MAIN_GATE_CONFIG_UNOBSTRUCTED_AUTOCLOSE_SECONDS,
                Constants.PREFERENCE_MAIN_GATE_CONFIG_UNOBSTRUCTED_AUTOCLOSE_SECONDS_DEFAULT
            )
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicTitle(text = context.getString(R.string.schedule_title_schedule))

                // Main gate
                BasicDivider(context.getString(R.string.settings_title_mainGate))
                BasicToggleSwitch(
                    isToggled = isDelayedAutocloseToggled,
                    onToggledChange = { isDelayedAutocloseToggled = it },
                    @@ -95, 66 + 92, 62 @@ fun ScheduleScreen() {
                        BasicToggleSwitch(
                            isToggled = isCrossedAutocloseToggled,
                            onToggledChange = { isCrossedAutocloseToggled = it },
                            label = context.getString(R.string.mainGate_label_crossedAutoclose)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        TextField(
                            value = crossedAutocloseSeconds.toString(),
                            onValueChange = { crossedAutocloseSeconds = it.trim().toInt() },
                            label = { Text(context.getString(R.string.schedule_label_crossedAutoclose)) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isEditMode
                        )
                    }

                            Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                            ) {
                        BasicButton(
                            title = context.getString(if (isEditMode) R.string.common_button_save else R.string.common_button_edit),
                            onClick = {
                                if (isEditMode) {
                                    preferences.setPreference(
                                        Constants.PREFERENCE_MAIN_GATE_CONFIG_DELAYED_AUTOCLOSE_ENABLED,
                                        isDelayedAutocloseToggled
                                    )
                                    preferences.setPreference(
                                        Constants.PREFERENCE_MAIN_GATE_CONFIG_UNOBSTRUCTED_AUTOCLOSE_ENABLED,
                                        isCrossedAutocloseToggled
                                    )
                                    preferences.setPreference(
                                        Constants.PREFERENCE_MAIN_GATE_CONFIG_DELAYED_AUTOCLOSE_SECONDS,
                                        delayedAutocloseSeconds
                                    )
                                    preferences.setPreference(
                                        Constants.PREFERENCE_MAIN_GATE_CONFIG_UNOBSTRUCTED_AUTOCLOSE_SECONDS,
                                        crossedAutocloseSeconds
                                    )

                                    Toasts.show(
                                        context,
                                        context.getString(R.string.settings_toast_saved)
                                    )
                                }
                                isEditMode = !isEditMode
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 32.dp, top = 16.dp, start = 16.dp, end = 16.dp)
                        )
                    }
            }
        }
    }

    @Preview
    @Composable
    fun ScheduleScreenPreview() {
        HomeHubTheme {
            ScheduleScreen()
        }
    }