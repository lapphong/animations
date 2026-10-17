package com.animations.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.animations.ui.theme.primary
import com.animations.ui.theme.trackColor

@Composable
fun ProgressBar(progress: () -> Float) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
    ) {
        val radius = CornerRadius(size.height / 2f)
        drawRoundRect(color = trackColor, cornerRadius = radius)

        val filled = size.width * progress().coerceIn(0f, 1f)
        if (filled > 0f) {
            drawRoundRect(
                color = primary,
                size = Size(filled, size.height),
                cornerRadius = radius,
            )
        }
    }
}
