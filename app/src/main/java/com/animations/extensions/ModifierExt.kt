package com.animations.extensions

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun Modifier.shimmerEffect(
    baseColor: Color = Color(0xFF1E88E5),
    highlightColor: Color = Color.LightGray
): Modifier = composed {
    val animatedGradient = rememberAnimatedGradient(baseColor, highlightColor)

    background(
        brush = animatedGradient.brush()
    ).onGloballyPositioned { layoutCoordinates -> animatedGradient.size = layoutCoordinates.size }
}
