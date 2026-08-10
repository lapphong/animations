package com.disintegration.extensions

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.get
import com.disintegration.Particle
import com.disintegration.Strip
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

internal fun ImageBitmap.buildEffect(
    numStrips: Int = 20,
    step: Int = 10,
): Pair<List<Particle>, List<Strip>> {
    val hardwareBmp = this.asAndroidBitmap()
    val bmp = hardwareBmp.copy(Bitmap.Config.ARGB_8888, false)
    val w = bmp.width
    val h = bmp.height

    val stripWidth = w / numStrips
    val strips = buildList {
        for (i in 0 until numStrips) {
            val x = i * stripWidth
            val sw = if (i == numStrips - 1) w - x else stripWidth
            val stripBmp = Bitmap.createBitmap(bmp, x, 0, sw, h)
            val normalizedX = x.toFloat() / w
            add(
                Strip(
                    bitmap = stripBmp.asImageBitmap(),
                    x = x,
                    width = sw,
                    height = h,
                    delay = normalizedX * 0.6f,
                )
            )
        }
    }

    val particles = buildList {
        for (y in 0 until h step step) {
            for (x in 0 until w step step) {
                val pixel = bmp[x, y]
                if (android.graphics.Color.alpha(pixel) < 50) continue
                val normalizedX = x.toFloat() / w
                val angle = -0.8f + Random.nextFloat() * 0.5f
                add(
                    Particle(
                        x = x.toFloat(),
                        y = y.toFloat(),
                        color = Color(pixel),
                        radius = step / 2f + Random.nextFloat() * 2f,
                        delay = normalizedX * 0.6f + Random.nextFloat() * 0.1f,
                        cosAngle = cos(angle),
                        sinAngle = sin(angle),
                        speed = 120f + Random.nextFloat() * 200f,
                        wobble = Random.nextFloat() * 6.28f,
                    )
                )
            }
        }
    }

    return particles to strips
}
