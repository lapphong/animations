package com.disintegration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class DisintegrationState(
    private val durationMs: Int = 500
) {
    internal var triggered by mutableStateOf(false)
        private set

    internal var progress by mutableFloatStateOf(0f)

    internal var particles by mutableStateOf<List<Particle>>(emptyList())

    internal var strips by mutableStateOf<List<Strip>>(emptyList())

    internal var onDone: (() -> Unit)? = null

    internal val duration
        get() = durationMs

    fun disintegrate(
        onComplete: () -> Unit = {}
    ) {
        onDone = onComplete
        triggered = true
    }

    internal fun finish() {
        onDone?.invoke()
        triggered = false
        progress = 0f
        particles = emptyList()
        strips = emptyList()
    }
}

@Composable
fun rememberDisintegrationState(durationMs: Int = 500) = remember { DisintegrationState(durationMs) }
