package com.animations.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.animations.shared.nav.CarouselSliderDemoKey
import com.animations.shared.nav.DisintegrationDemoKey
import com.animations.utils.FlutterEngineManager
import com.animations.utils.openFlutterScreen
import com.animations.widgets.EmbeddedFlutterView
import com.animations.widgets.GradientButton
import com.animations.widgets.GradientText
import com.animations.widgets.HeaderView

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavKey) -> Unit = {}
) {
    val context = LocalContext.current
    var counter by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderView("Animation App", leftIcon = 0)
            if (counter.isNotBlank()) {
                GradientText(counter)
            }
            Routes.entries.forEach {
                GradientButton(
                    modifier = Modifier.padding(top = 12.dp),
                    text = it.label,
                    onClick = {
                        when (it) {
                            Routes.FLUTTER_COUNTER_DEMO -> {
                                context.openFlutterScreen { result -> counter = result }
                            }

                            Routes.SEND_DATA_TO_FLUTTER -> {
                                FlutterEngineManager.sendDataToFlutter("Data from Android Native, la la")
                                context.openFlutterScreen { result -> counter = result }
                            }

                            else -> {
                                it.navKey?.let { key -> onNavigate(key) }
                            }
                        }
                    }
                )
            }
        }

        EmbeddedFlutterView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
    }
}

enum class Routes(val navKey: NavKey?) {
    FLUTTER_COUNTER_DEMO(null),
    DISINTEGRATION_DEMO(DisintegrationDemoKey),
    CAROUSEL_SLIDER_DEMO(CarouselSliderDemoKey("Data from HomeScreen")),
    SEND_DATA_TO_FLUTTER(null);

    val label: String
        get() = "Go to ${
            when (this) {
                DISINTEGRATION_DEMO -> "DisintegrationDemo"
                CAROUSEL_SLIDER_DEMO -> "CarouselSliderDemo"
                FLUTTER_COUNTER_DEMO -> "Flutter Screen"
                SEND_DATA_TO_FLUTTER -> "+ Send data to Flutter Screen"
            }
        }"
}