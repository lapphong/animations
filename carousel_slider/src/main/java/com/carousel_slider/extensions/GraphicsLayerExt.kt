package com.carousel_slider.extensions

import androidx.compose.ui.graphics.GraphicsLayerScope
import com.carousel_slider.ALPHA_DROP
import com.carousel_slider.MAX_ROTATION
import com.carousel_slider.MIN_ALPHA
import com.carousel_slider.MIN_SCALE
import com.carousel_slider.OVERLAP
import com.carousel_slider.SCALE_DROP
import kotlin.math.abs
import kotlin.math.sign

internal fun GraphicsLayerScope.cardTransform(offset: Float) {
    val ax = abs(offset)
    cameraDistance = 14f * density
    rotationY = offset.coerceIn(-1f, 1f) * MAX_ROTATION
    val scale = (1f - ax * SCALE_DROP).coerceAtLeast(MIN_SCALE)
    scaleX = scale
    scaleY = scale
    alpha = (1f - ax * ALPHA_DROP).coerceAtLeast(MIN_ALPHA)
    val shift = if (ax <= 1f) offset * OVERLAP
    else offset.sign * (OVERLAP + (ax - 1f) * 0.16f)
    translationX = shift * size.width
}
