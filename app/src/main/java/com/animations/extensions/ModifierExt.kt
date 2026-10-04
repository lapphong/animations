package com.animations.extensions

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag

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

private var lastClickAtMs = 0L
private const val CLICK_DEBOUNCE_MS = 400L
private fun isDebounced(): Boolean {
    val now = SystemClock.elapsedRealtime()
    if (now - lastClickAtMs < CLICK_DEBOUNCE_MS) return true
    lastClickAtMs = now
    return false
}

@Composable
fun Modifier.onClickNotRipple(
    clickName: String,
    interactionSource: MutableInteractionSource? = null,
    click: () -> Unit,
): Modifier {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    return this
        .testTag(clickName)
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            if (isDebounced()) return@clickable
            click()
        }
}
