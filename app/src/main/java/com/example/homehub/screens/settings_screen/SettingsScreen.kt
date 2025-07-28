package com.example.homehub.screens.settings_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.homehub.R
import com.example.homehub.composables.BasicButton
import com.example.homehub.composables.BasicTitle
import com.example.homehub.constants.Constants
import com.example.homehub.ui.theme.HomeHubTheme
import com.example.homehub.utils.Preferences
import com.example.homehub.utils.Toasts
import com.example.homehub.viewmodels.SettingsScreenViewModel

@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel? = null) {

    // Collecting events (Toast clearly)
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    ToastSingleton.show(context, event.message)
                    // Or directly:
                    // Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val context = LocalContext.current
    var isEditMode by remember { mutableStateOf(false) }

    var mqttHost by remember {
        mutableStateOf(
            Preferences.getPreference<String>(
                context,
                Constants.PREFERENCE_MQTT_HOST,
                ""
            )
        )
    }
    var mqttPort by remember {
        mutableStateOf(
            Preferences.getPreference<String>(
                context,
                Constants.PREFERENCE_MQTT_PORT,
                ""
            )
        )
    }
    var mqttUser by remember {
        mutableStateOf(
            Preferences.getPreference<String>(
                context,
                Constants.PREFERENCE_MQTT_USER,
                ""
            )
        )
    }
    var mqttPass by remember {
        mutableStateOf(
            Preferences.getPreference<String>(
                context,
                Constants.PREFERENCE_MQTT_PASS,
                ""
            )
        )
    }
    val userId = Preferences.getPreference<String>(context, Constants.PREFERENCE_USER_ID, "")

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
                BasicTitle(text = stringResource(R.string.settings_title_settings))
                MqttSettingsSection(
                    isEditMode = isEditMode,
                    mqttHost = mqttHost,
                    onMqttHostChange = { mqttHost = it },
                    mqttPort = mqttPort,
                    onMqttPortChange = { mqttPort = it },
                    mqttUser = mqttUser,
                    onMqttUserChange = { mqttUser = it },
                    mqttPass = mqttPass,
                    onMqttPassChange = { mqttPass = it }
                )
                UserSettingsSection(userId = userId)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                BasicButton(
                    title = stringResource(if (isEditMode) R.string.common_button_save else R.string.common_button_edit),
                    onClick = {
                        if (isEditMode) {
                            Preferences.setPreference<String>(
                                context,
                                Constants.PREFERENCE_MQTT_HOST,
                                mqttHost
                            )
                            Preferences.setPreference<String>(
                                context,
                                Constants.PREFERENCE_MQTT_PORT,
                                mqttPort
                            )
                            Preferences.setPreference<String>(
                                context,
                                Constants.PREFERENCE_MQTT_USER,
                                mqttUser
                            )
                            Preferences.setPreference<String>(
                                context,
                                Constants.PREFERENCE_MQTT_PASS,
                                mqttPass
                            )
                            Toasts.show(context, context.getString(R.string.settings_toast_saved))
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
fun SettingsScreenPreview() {
    HomeHubTheme {
        SettingsScreen()
    }
}