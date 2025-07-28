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
fun SettingsScreenUserSection(userId: String) {
    BasicDivider(text = stringResource(R.string.settings_title_userData))
    TextField(
        value = userId,
        onValueChange = {},
        label = { Text(stringResource(R.string.settings_label_userId)) },
        modifier = Modifier.fillMaxWidth(),
        enabled = false
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview
@Composable
fun SettingsScreenUserSectionPreview() {
    SettingsScreenUserSection(userId = "demo-user-id")
}
