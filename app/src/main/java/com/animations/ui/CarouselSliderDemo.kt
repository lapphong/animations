package com.animations.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.animations.widgets.HeaderView
import com.carousel_slider.CarouselDefaults
import com.carousel_slider.CarouselSlider

private val covers = listOf(
    "https://picsum.photos/id/1084/600/600",
    "https://picsum.photos/id/1080/600/600",
    "https://picsum.photos/id/1074/600/600",
    "https://picsum.photos/id/1062/600/600",
    "https://picsum.photos/id/1060/600/600",
    "https://picsum.photos/id/1059/600/600",
    "https://picsum.photos/id/1050/600/600",
    "https://picsum.photos/id/1044/600/600",
)

private val darkBg = Color(0xFF0B0B0F)

@Composable
fun CarouselSliderDemo(
    modifier: Modifier = Modifier,
    title: String,
    onBackPressed: () -> Unit = {}
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1A22), darkBg)))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderView(title = title, onLeftClick = onBackPressed)
            Spacer(Modifier.height(32.dp))
            Text(
                text = "Cover Flow",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Swipe through • tap a cover to focus it",
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CarouselSlider(
                    items = covers,
                    options = CarouselDefaults.Options.copy(
                        cardSpacing = 30.dp,
                        background = darkBg,
                    ),
                    onItemClick = {
                        Log.d("CoverFlow", "Click ảnh $it, value: ${covers[it]}")
                    }
                )
            }
        }
    }
}
