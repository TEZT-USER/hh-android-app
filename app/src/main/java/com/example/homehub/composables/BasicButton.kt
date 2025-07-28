package com.example.homehub.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homehub.ui.theme.HomeHubTheme

@Composable
fun BasicButton(
    title: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 16.sp),
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val loaderSize = (textStyle.fontSize.value * 1.5f).dp

    Button(
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(),
        shape = RoundedCornerShape(6.dp),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            Box(modifier = Modifier.size(loaderSize)) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 2.dp
                )
            }
        } else {
            Text(
                text = title,
                style = textStyle,
            )
        }
    }
}

@Preview(widthDp = 300)
@Composable
fun PreviewBasicButton() {
    HomeHubTheme {
        BasicButton(
            title = "Click",
            onClick = {}
        )
    }
}
