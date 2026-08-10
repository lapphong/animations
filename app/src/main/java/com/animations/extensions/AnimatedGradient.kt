package com.animations.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

@Composable
fun rememberAnimatedGradient(
    baseColor: Color = Color(0xFF1E88E5),
    highlightColor: Color = Color.LightGray,
    durationMillis: Int = 2000,
): AnimatedGradient {
    return remember {
        AnimatedGradient(
            baseColor = baseColor,
            highlightColor = highlightColor,
            durationMillis = durationMillis
        )
    }
}

class AnimatedGradient(
    private val baseColor: Color,
    private val highlightColor: Color,
    private val durationMillis: Int,
) {
    var size by mutableStateOf(IntSize.Zero)

    @Composable
    fun brush(): Brush {
        val transition = rememberInfiniteTransition(label = "animatedGradient")

        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                )
            ),
            label = "animatedGradientXOffset"
        )

        return Brush.linearGradient(
            colors = listOf(baseColor, highlightColor, baseColor),
            start = Offset(
                x = startOffsetX,
                y = -size.height.toFloat()
            ),
            end = Offset(
                x = startOffsetX + size.width.toFloat(),
                y = size.height.toFloat() * 2
            )
        )
    }
}
