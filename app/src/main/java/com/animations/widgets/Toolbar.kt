package com.animations.widgets


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPressed: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier.size(32.dp),
                contentDescription = "",
                tint = Color(0xFF1E88E5),
            )
        }
        GradientText(title)
        Spacer(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ToolBarPreview() {
    ToolBar(
        title = "Test",
        onBackPressed = {}
    )
}
