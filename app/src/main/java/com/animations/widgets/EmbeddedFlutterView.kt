package com.animations.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.animations.shared.extensions.roundRadius
import com.animations.shared.extensions.topBorder
import com.animations.utils.FlutterEngineManager
import io.flutter.embedding.android.FlutterView
import org.koin.compose.koinInject

@Composable
fun EmbeddedFlutterView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val engineManager = koinInject<FlutterEngineManager>()
    val engine = remember { engineManager.engine() } ?: return
    val flutterView = remember(context) { FlutterView(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val reattachSignal by engineManager.viewReattachSignal.collectAsState()

    DisposableEffect(lifecycleOwner, flutterView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    engine.lifecycleChannel.appIsResumed()
                    flutterView.attachToFlutterEngine(engine)
                }

                Lifecycle.Event.ON_PAUSE -> {
                    engine.lifecycleChannel.appIsInactive()
                    flutterView.detachFromFlutterEngine()
                }

                Lifecycle.Event.ON_STOP -> engine.lifecycleChannel.appIsPaused()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            flutterView.detachFromFlutterEngine()
        }
    }

    // The full-screen Flutter Activity may close after Home's ON_RESUME, leaving Dart detached.
    // Reattach the view to resume rendering.
    LaunchedEffect(reattachSignal) {
        if (reattachSignal == 0) return@LaunchedEffect
        engine.lifecycleChannel.appIsResumed()
        flutterView.detachFromFlutterEngine()
        flutterView.attachToFlutterEngine(engine)
    }

    AndroidView(
        modifier = modifier
            .topBorder()
            .padding(25.dp)
            .clip(RoundedCornerShape(20.dp))
            .roundRadius(),
        factory = { flutterView.apply { attachToFlutterEngine(engine) } },
    )
}
