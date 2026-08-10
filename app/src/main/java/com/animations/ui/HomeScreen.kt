package com.animations.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.animations.nav.CarouselSliderDemoKey
import com.animations.nav.DisintegrationDemoKey
import com.animations.widgets.GradientButton
import com.animations.widgets.GradientText

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavKey) -> Unit = {}
) {
    Column(
        modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GradientText("Animation App")
        Spacer(Modifier.padding(vertical = 6.dp))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Routes.entries.forEach {
                    GradientButton(
                        modifier = Modifier.padding(top = 12.dp),
                        text = it.label,
                        onClick = { onNavigate(it.navKey) }
                    )
                }
            }
        }
    }
}

enum class Routes(val navKey: NavKey) {
    DISINTEGRATION_DEMO(DisintegrationDemoKey),
    CAROUSEL_SLIDER_DEMO(CarouselSliderDemoKey("Data from HomeScreen"));

    val label: String
        get() = "Go to ${
            when (this) {
                DISINTEGRATION_DEMO -> "DisintegrationDemo"
                CAROUSEL_SLIDER_DEMO -> "CarouselSliderDemo"
            }
        }"
}