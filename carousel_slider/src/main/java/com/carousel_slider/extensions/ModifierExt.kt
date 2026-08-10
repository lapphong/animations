package com.carousel_slider.extensions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

internal fun Modifier.cardOverlay(
    state: PagerState,
    page: Int,
): Modifier = drawBehind {
    val ax = abs((state.currentPage - page) + state.currentPageOffsetFraction)

    drawRect(
        color = Color.Black,
        alpha = (ax * 0.5f).coerceAtMost(0.55f),
    )
}

internal fun Modifier.cardTapGesture(
    halfCardPx: Float,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
): Modifier = pointerInput(halfCardPx) {
    detectTapGestures { pos ->
        val center = size.width / 2f
        when {
            pos.x < center - halfCardPx -> onPrevious()
            pos.x > center + halfCardPx -> onNext()
        }
    }
}
