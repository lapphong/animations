package com.animations.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.animations.widgets.GradientButton
import com.animations.widgets.HeaderView
import com.animations.widgets.ListTile
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.disintegration.DisintegrationEffect
import com.disintegration.rememberDisintegrationState
import kotlinx.coroutines.launch

private val photos = listOf(
    "https://picsum.photos/id/29/800/600",
    "https://picsum.photos/id/180/800/600",
    "https://picsum.photos/id/169/800/600",
    "https://picsum.photos/id/40/800/600",
    "https://picsum.photos/id/15/800/600",
)

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DisintegrationDemo(
    modifier: Modifier = Modifier,
    goToCarouselSlider: (String) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val state = rememberDisintegrationState(durationMs = 500)
    var currentIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val appearScale = remember { Animatable(1f) }
    val appearAlpha = remember { Animatable(1f) }
    var items by remember { mutableStateOf(photos) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1A22), Color(0xFF0B0B0F))))
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderView(
            title = "Disintegration Demo",
            onLeftClick = onBackPressed
        )
        GradientButton(
            modifier = Modifier
                .widthIn(max = 250.dp)
                .padding(vertical = 8.dp),
            text = "Open CarouselSlider Demo",
            onClick = { goToCarouselSlider("Data from DisintegrationDemo") },
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${currentIndex + 1} / ${photos.size}",
            textAlign = TextAlign.End,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            DisintegrationEffect(
                modifier = Modifier.fillMaxSize(),
                state
            ) {
                GlideImage(
                    model = photos[currentIndex],
                    contentDescription = "Photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = appearScale.value
                            scaleY = appearScale.value
                            alpha = appearAlpha.value
                        }
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            LazyColumn(
                Modifier
                    .animateContentSize(
                        animationSpec = keyframes {
                            durationMillis = 300
                        }
                    )
                    .padding(horizontal = 12.dp)
            ) {
                items(
                    items = items,
                    key = { it }
                ) { item ->
                    ListTile(
                        text = item,
                        onDelete = {
                            items = items - item
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(32.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    items = photos
                    state.disintegrate {
                        currentIndex = (currentIndex + 1) % photos.size
                        scope.launch {
                            appearScale.snapTo(0.8f)
                            appearAlpha.snapTo(0f)
                            launch { appearScale.animateTo(1f, tween(500)) }
                            launch { appearAlpha.animateTo(1f, tween(500)) }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DE9B6)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Next Photo")
            }
        }
    }
}
