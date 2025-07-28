package com.example.homehub.screens.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.homehub.enums.MainGateStateEnum
import com.example.homehub.states.MainGateState
import com.example.homehub.ui.theme.HomeHubTheme
import com.example.homehub.viewmodels.MainScreenViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MainScreenMainGateCard(viewModel = viewModel)
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    HomeHubTheme {
        MainScreenMainGateCardContent(
            gateStatus = MainGateState(
                state = MainGateStateEnum.CLOSED,
                isObstructed = false
            ),
            isButtonEnabled = false,
            isButtonLoading = false,
            onActivate = {}
        )
    }
}