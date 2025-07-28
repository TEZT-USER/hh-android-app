package com.example.homehub.screens.logs_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.homehub.R
import com.example.homehub.composables.BasicTitle
import com.example.homehub.ui.theme.HomeHubTheme
import kotlinx.coroutines.delay

@Composable
fun LogsScreen() {
    val context = LocalContext.current
    var logContent by remember { mutableStateOf("") }

    // Load the log content when the composable is first composed
    LaunchedEffect(Unit) {
        // Initial load of log content
        logContent = "XXX"//Logs.getLogFileContent()

        // Start a loop to update log content every few seconds
        while (true) {
            delay(2000) // Update every 2 seconds
            val newLogContent = "XXX"//Logs.getLogFileContent()
            if (newLogContent != logContent) {
                logContent = newLogContent // Update if content has changed
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTitle(text = context.getString(R.string.logs_title_logs))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${context.getString(R.string.app_version)}: v${
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        0
                    ).versionName
                }",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = logContent,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun LogsScreenPreview() {
    HomeHubTheme {
        LogsScreen()
    }
}
