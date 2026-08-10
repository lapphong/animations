package com.animations.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.disintegration.DisintegrationEffect
import com.disintegration.rememberDisintegrationState
import kotlinx.coroutines.launch

@Composable
fun ListTile(
    modifier: Modifier = Modifier,
    text: String,
    onDelete: () -> Unit,
) {
    val state = rememberDisintegrationState(durationMs = 1000)
    val scope = rememberCoroutineScope()
    val appearScale = remember { Animatable(1f) }
    val appearAlpha = remember { Animatable(1f) }

    DisintegrationEffect(modifier.padding(vertical = 10.dp), state = state) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .heightIn(min = 36.dp)
                .border(
                    width = 1.dp, color = Color.Transparent,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(Color.Red)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = text)
                Icon(
                    Icons.Filled.Delete,
                    modifier = Modifier.clickable(
                        onClick = {
                            state.disintegrate {
                                scope.launch {
                                    appearScale.snapTo(0.8f)
                                    appearAlpha.snapTo(0f)
                                    launch { appearScale.animateTo(1f, tween(500)) }
                                    launch { appearAlpha.animateTo(1f, tween(500)) }
                                }
                                onDelete()
                            }
                        }
                    ),
                    contentDescription = "",
                )
            }
        }
    }
}
