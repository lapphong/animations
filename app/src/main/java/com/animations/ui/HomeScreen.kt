package com.animations.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.animations.R
import com.animations.models.OpenTo
import com.animations.shared.nav.CarouselSliderDemoKey
import com.animations.shared.nav.DisintegrationDemoKey
import com.animations.utils.FlutterEngineManager
import com.animations.utils.openFlutterScreen
import com.animations.utils.settings.AppSettings
import com.animations.widgets.AppLottie
import com.animations.widgets.CenterColumn
import com.animations.widgets.EmbeddedFlutterView
import com.animations.widgets.GradientButton
import com.animations.widgets.GradientText
import com.animations.widgets.HeaderView
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavKey) -> Unit = {}
) {
    val context = LocalContext.current
    val engineManager = koinInject<FlutterEngineManager>()
    val counter by engineManager.flutterMessage.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (AppSettings.openTo == OpenTo.OPEN_TO_FLUTTER_SCREEN.name) {
            AppSettings.openTo = OpenTo.OPEN_TO_DEFAULT.name
            context.openFlutterScreen()
        }
    }

    var showIdleLottie by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2_000.milliseconds)
        showIdleLottie = true
    }

    CenterColumn(
        modifier = modifier.fillMaxSize(),
        itemSpacing = 20.dp
    ) {
        CenterColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            HeaderView("Animation App", leftIcon = 0)
            counter?.takeIf { it.isNotBlank() }?.let { GradientText(it) }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clipToBounds()
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(top = BUTTON_SPACING),
                    verticalArrangement = Arrangement.spacedBy(BUTTON_SPACING),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    itemsIndexed(
                        items = Routes.entries,
                        key = { _, route -> route.name },
                    ) { _, route ->
                        GradientButton(
                            text = route.label,
                            onClick = {
                                when (route) {
                                    Routes.FLUTTER_COUNTER_DEMO -> {
                                        context.openFlutterScreen()
                                    }

                                    Routes.SEND_DATA_TO_FLUTTER -> {
                                        engineManager.sendDataToFlutter("Data from Android Native, la la")
                                        context.openFlutterScreen()
                                    }

                                    else -> {
                                        route.navKey?.let { key -> onNavigate(key) }
                                    }
                                }
                            }
                        )
                    }
                }

                if (showIdleLottie) {
                    val targetIndex = 0

                    val targetVisible = remember(listState, targetIndex) {
                        derivedStateOf { listState.layoutInfo.onScreenItem(targetIndex) != null }
                    }.value

                    AppLottie(
                        res = R.raw.lottie_click,
                        isPlaying = targetVisible,
                        modifier = Modifier
                            .layout { measurable, constraints ->
                                val hint = 130.dp.roundToPx()
                                val placeable = measurable.measure(Constraints.fixed(hint, hint))
                                layout(constraints.maxWidth, constraints.maxHeight) {
                                    placeable.place(constraints.maxWidth - hint, 0)
                                }
                            }
                            .graphicsLayer {
                                val target = listState.layoutInfo.onScreenItem(targetIndex)
                                alpha = if (target == null) 0f else 1f
                                translationX = 23.dp.toPx()
                                translationY = (target?.offset ?: 0).toFloat() - 12.dp.toPx()
                            },
                    )
                }
            }
        }

        EmbeddedFlutterView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
    }
}

private val BUTTON_SPACING = 12.dp

internal fun LazyListLayoutInfo.onScreenItem(index: Int): LazyListItemInfo? =
    visibleItemsInfo.firstOrNull {
        it.index == index && it.offset + it.size > 0 && it.offset < viewportEndOffset
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
