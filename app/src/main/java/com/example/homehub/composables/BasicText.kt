package com.example.homehub.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun BasicText(text: String, size: TextUnit = 16.sp) {
    Text(text = text, fontSize = size)
}

@Preview
@Composable
fun PreviewBasicText() {
    BasicText("Title", size = 20.sp)
}

