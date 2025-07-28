package com.example.homehub.screens.main_screen

import com.example.homehub.composables.BasicButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homehub.R
import com.example.homehub.composables.BasicText
import com.example.homehub.composables.BasicTitle
import com.example.homehub.enums.MainGateStateEnum
import com.example.homehub.enums.toUiText
import com.example.homehub.states.MainGateState
import com.example.homehub.utils.Toasts
import com.example.homehub.viewmodels.MainScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreenMainGateCardContent(
    gateStatus: MainGateState?,
    isButtonEnabled: Boolean,
    isButtonLoading: Boolean,
    onActivate: () -> Unit
) {
    val context = LocalContext.current

    val buttonTitle = if (
        gateStatus?.state == MainGateStateEnum.OPEN ||
        gateStatus?.state == MainGateStateEnum.OPENING
    ) {
        context.getString(R.string.mainGate_button_close)
    } else {
        context.getString(R.string.mainGate_button_open)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTitle(text = context.getString(R.string.mainGate_title_mainGate))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicText(
                    text = gateStatus?.state?.toUiText(context) ?: "",
                    size = 20.sp
                )
                Spacer(modifier = Modifier.height(64.dp))
                BasicButton(
                    title = buttonTitle,
                    isLoading = isButtonLoading,
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .aspectRatio(1f),
                    textStyle = TextStyle(fontSize = 28.sp),
                    enabled = isButtonEnabled,
                    onClick = onActivate
                )
            }
        }
    }
}

@Composable
fun MainScreenMainGateCard(viewModel: MainScreenViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val mainGateStatus by viewModel.mainGateStatus.collectAsState()
    var isButtonEnabled by remember { mutableStateOf(true) }
    var isButtonLoading by remember { mutableStateOf(false) }

    fun handleActivateGate() {
        if (isButtonLoading || !isButtonEnabled) return

        isButtonLoading = true
        isButtonEnabled = false
        val startTime = System.currentTimeMillis()

        coroutineScope.launch {
            viewModel.activateMainGate().collect { success ->
                if (success) {
                    Toasts.show(
                        context,
                        if (mainGateStatus?.state == MainGateStateEnum.OPEN || mainGateStatus?.state == MainGateStateEnum.OPENING)
                            context.getString(R.string.mainGate_toast_closing)
                        else
                            context.getString(R.string.mainGate_toast_opening)
                    )
                } else {
                    Toasts.show(
                        context,
                        context.getString(R.string.mainGate_toast_couldNotActivate)
                    )
                }

                isButtonLoading = false
                val elapsed = System.currentTimeMillis() - startTime
                delay((2000 - elapsed).coerceAtLeast(0L))
                isButtonEnabled = true
            }
        }
    }

    MainScreenMainGateCardContent(
        gateStatus = mainGateStatus,
        isButtonEnabled = viewModel.isFormEnabled && isButtonEnabled,
        isButtonLoading = isButtonLoading,
        onActivate = { handleActivateGate() }
    )
}

@Preview
@Composable
fun MainScreenMainGateCardPreview() {
    MainScreenMainGateCardContent(
        gateStatus = MainGateState(
            state = MainGateStateEnum.CLOSED,
            isObstructed = false
        ),
        isButtonEnabled = true,
        isButtonLoading = false,
        onActivate = {}
    )
}
