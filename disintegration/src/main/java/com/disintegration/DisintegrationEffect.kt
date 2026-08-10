package com.disintegration

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import com.disintegration.extensions.buildEffect
import com.disintegration.extensions.drawParticles
import com.disintegration.extensions.drawStrips
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DisintegrationEffect(
    modifier: Modifier = Modifier,
    state: DisintegrationState,
    content: @Composable () -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()

    LaunchedEffect(state.triggered) {
        if (state.triggered.not()) return@LaunchedEffect
        val bitmap = graphicsLayer.toImageBitmap()

        val result = withContext(Dispatchers.Default) { bitmap.buildEffect() }
        state.particles = result.first
        state.strips = result.second
        val startTime = withFrameMillis { it }
        while (true) {
            val elapsed = withFrameMillis { it } - startTime
            val p = (elapsed.toFloat() / state.duration).coerceAtMost(1f)
            state.progress = p
            if (p >= 1f) break
        }
        state.finish()
    }

    Box(
        modifier = modifier.drawWithContent {
            graphicsLayer.record { this@drawWithContent.drawContent() }
            if (!state.triggered) {
                drawLayer(graphicsLayer)
            } else if (state.strips.isEmpty()) {
                drawLayer(graphicsLayer)
            } else {
                drawStrips(state.strips, state.progress)
                drawParticles(state.particles, state.progress)
            }
        }
    ) {
        content()
    }
}
