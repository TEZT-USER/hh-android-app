package com.example.homehub.activities

import com.example.homehub.screens.logs_screen.LogsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.example.homehub.screens.main_screen.MainScreen
import com.example.homehub.screens.settings_screen.SettingsScreen
import com.example.homehub.ui.theme.HomeHubTheme
import com.example.homehub.viewmodels.LogsScreenViewModel
import com.example.homehub.viewmodels.MainScreenViewModel
import com.example.homehub.viewmodels.SettingsScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    private val settingsScreenViewModel: SettingsScreenViewModel by viewModels()
    private val logsScreenViewModel: LogsScreenViewModel by viewModels()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            HomeHubTheme {
                val pagerState = rememberPagerState(pageCount = { 3 })
                HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> MainScreen(mainScreenViewModel)
                        1 -> SettingsScreen()
                        2 -> LogsScreen()
                    }
                }
            }
        }
    }
}
