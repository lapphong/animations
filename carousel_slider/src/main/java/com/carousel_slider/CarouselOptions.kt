package com.carousel_slider

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class CarouselOptions(
    val cardSize: Dp = 240.dp,
    val cardHeight: Dp = cardSize * 1.4f,
    val cardSpacing: Dp = 0.dp,
    val reflection: Boolean = true,
    val background: Color = Color(0xFF0B0B0F),
    val autoPlay: Boolean = true,
    val autoPlayInterval: Long = 1000L,
)

object CarouselDefaults {
    val Options = CarouselOptions()
}
