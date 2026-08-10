package com.disintegration.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.disintegration.Particle
import com.disintegration.Strip
import kotlin.math.sin

internal fun DrawScope.drawStrips(strips: List<Strip>, p: Float) {
    val paint = Paint()
    drawIntoCanvas { canvas ->
        for (strip in strips) {
            val localP = ((p - strip.delay) / 0.3f).coerceIn(0f, 1f)
            val alpha = 1f - localP
            if (alpha <= 0f) continue
            paint.alpha = alpha
            canvas.drawImageRect(
                image = strip.bitmap,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(strip.bitmap.width, strip.bitmap.height),
                dstOffset = IntOffset(strip.x, 0),
                dstSize = IntSize(strip.width, strip.height),
                paint = paint
            )
        }
    }
}

internal fun DrawScope.drawParticles(particles: List<Particle>, p: Float) {
    val size = particles.size
    for (i in 0 until size) {
        val pt = particles[i]
        if (p <= pt.delay) continue
        val localP = ((p - pt.delay) / (1f - pt.delay)).coerceAtMost(1f)

        val drift = pt.speed * localP
        val wx = sin(localP * 12f + pt.wobble) * 20f * localP
        val dx = pt.cosAngle * drift + wx
        val dy = pt.sinAngle * drift
        val alpha = 1f - localP * localP
        if (alpha <= 0f) continue

        drawCircle(
            color = pt.color,
            radius = pt.radius,
            center = Offset(pt.x + dx, pt.y + dy),
            alpha = alpha
        )
    }
}
