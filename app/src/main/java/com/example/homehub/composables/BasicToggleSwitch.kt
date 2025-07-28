package com.example.homehub.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicToggleSwitch(
    isToggled: Boolean,
    onToggledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        if (label != null) {
            Text(
                text = label,
                fontSize = 18.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )
        }
        Switch(
            checked = isToggled,
            onCheckedChange = onToggledChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BasicToggleSwitchPreview() {
    BasicToggleSwitch(
        isToggled = true,
        onToggledChange = {},
        label = "Lorem ipsum do lor sit amet, con sec te tur ad ip isc ing elit, sed do",
    )
}

