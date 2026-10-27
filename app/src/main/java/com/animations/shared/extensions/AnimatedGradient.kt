package com.animations.shared.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

/** 15 fps: a 2 s sweep looks the same and the host recomposes 4x less than at 60 fps. */
private const val FRAME_MS = 66L

@Composable
fun rememberAnimatedGradient(
    baseColor: Color = Color(0xFF1E88E5),
    highlightColor: Color = Color.LightGray,
    durationMillis: Int = 2000,
): AnimatedGradient = remember(baseColor, highlightColor, durationMillis) {
    AnimatedGradient(baseColor, highlightColor, durationMillis)
}

class AnimatedGradient(
    private val baseColor: Color,
    private val highlightColor: Color,
    private val durationMillis: Int,
) {
    var size by mutableStateOf(IntSize.Zero)

    private var progress by mutableFloatStateOf(0f)

    @Composable
    fun brush(): Brush {
        LaunchedEffect(durationMillis, size.width) {
            if (size.width == 0) return@LaunchedEffect
            var start = 0L
            var last = 0L
            while (true) {
                withFrameMillis { now ->
                    if (start == 0L) start = now
                    if (now - last >= FRAME_MS) {
                        last = now
                        progress = ((now - start) % durationMillis) / durationMillis.toFloat()
                    }
                }
            }
        }

        val width = size.width.toFloat()
        val startOffsetX = -2 * width + progress * 4 * width
        return Brush.linearGradient(
            colors = listOf(baseColor, highlightColor, baseColor),
            start = Offset(
                x = startOffsetX,
                y = -size.height.toFloat()
            ),
            end = Offset(
                x = startOffsetX + width,
                y = size.height.toFloat() * 2
            )
        )
    }
}
