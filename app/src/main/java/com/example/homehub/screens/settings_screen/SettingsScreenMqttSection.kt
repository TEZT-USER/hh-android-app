package com.example.homehub.screens.settings_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.homehub.R
import com.example.homehub.composables.BasicDivider

@Composable
fun SettingsScreenMqttSection(
    isEditMode: Boolean,
    mqttHost: String,
    onMqttHostChange: (String) -> Unit,
    mqttPort: String,
    onMqttPortChange: (String) -> Unit,
    mqttUser: String,
    onMqttUserChange: (String) -> Unit,
    mqttPass: String,
    onMqttPassChange: (String) -> Unit
) {
    BasicDivider(text = stringResource(R.string.settings_title_mqttBroker))
    TextField(
        value = mqttHost,
        onValueChange = { onMqttHostChange(it.trim()) },
        label = { Text(stringResource(R.string.settings_label_mqttHost)) },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEditMode
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        value = mqttPort,
        onValueChange = { onMqttPortChange(it.trim()) },
        label = { Text(stringResource(R.string.settings_label_mqttPort)) },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEditMode
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        value = mqttUser,
        onValueChange = { onMqttUserChange(it.trim()) },
        label = { Text(stringResource(R.string.settings_label_mqttUser)) },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEditMode
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        value = mqttPass,
        onValueChange = { onMqttPassChange(it.trim()) },
        label = { Text(stringResource(R.string.settings_label_mqttPass)) },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEditMode
    )
}

@Preview
@Composable
fun SettingsScreenMqttSectionPreview() {
    SettingsScreenMqttSection(
        isEditMode = true,
        mqttHost = "broker.example.com",
        onMqttHostChange = {},
        mqttPort = "1883",
        onMqttPortChange = {},
        mqttUser = "user",
        onMqttUserChange = {},
        mqttPass = "password",
        onMqttPassChange = {}
    )
}
