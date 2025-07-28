package com.example.homehub.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BasicTitle(text: String) {
    Text(text, style = MaterialTheme.typography.headlineMedium)
}

@Preview
@Composable
fun PreviewBasicTitle() {
    BasicTitle("Title")
}