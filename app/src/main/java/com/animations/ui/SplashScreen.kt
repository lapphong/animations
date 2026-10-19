package com.animations.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.animations.R
import com.animations.widgets.AppImage
import com.animations.widgets.CenterBox
import com.animations.widgets.CenterColumn
import com.animations.widgets.ProgressBar

private const val PROGRESS_TARGET = 0.99f
private const val PROGRESS_DURATION_MS = 700

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {},
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = PROGRESS_TARGET,
            animationSpec = tween(PROGRESS_DURATION_MS, easing = LinearOutSlowInEasing)
        )
        onFinished()
    }

    Box(modifier.fillMaxSize()) {
        AppImage(
            res = R.drawable.img_splash_background,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        CenterColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp),
        ) {
            CenterBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                CenterColumn {
                    AppImage(
                        res = R.drawable.img_app_logo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(20.dp)),
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 24.dp),
                    )
                }
            }
            ProgressSection(progress = { progress.value })
        }
    }
}

@Composable
private fun ProgressSection(progress: () -> Float) {
    CenterColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp),
        itemSpacing = 8.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyLarge,
            )
            val percent by remember { derivedStateOf { (progress() * 100).toInt() } }
            Text(
                text = "$percent%",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        ProgressBar(progress)

        Text(
            text = stringResource(R.string.this_action_might_contain_ads),
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
            lineHeight = 15.sp,
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
