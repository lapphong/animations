package com.disintegration

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

internal data class Particle(
    val x: Float,
    val y: Float,
    val color: Color,
    val radius: Float,
    val delay: Float,
    val cosAngle: Float,
    val sinAngle: Float,
    val speed: Float,
    val wobble: Float,
)

internal data class Strip(
    val bitmap: ImageBitmap,
    val x: Int,
    val width: Int,
    val height: Int,
    val delay: Float,
)
