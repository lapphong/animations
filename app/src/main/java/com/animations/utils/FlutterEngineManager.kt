package com.animations.utils

import android.app.Application
import android.content.Context
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class FlutterEngineManager(application: Application) {
    private val _viewReattachSignal = MutableStateFlow(0)

    /** Increments whenever the embedded FlutterView needs to be reattached after a full-screen Flutter Activity closes. */
    val viewReattachSignal: StateFlow<Int> = _viewReattachSignal.asStateFlow()

    private val _flutterMessage = MutableStateFlow<String?>(null)
    val flutterMessage: StateFlow<String?> = _flutterMessage.asStateFlow()

    private var _methodChannel: MethodChannel? = null

    init {
        try {
            val engine = FlutterEngine(application)
            _methodChannel = MethodChannel(engine.dartExecutor.binaryMessenger, CHANNEL)
            _methodChannel?.setMethodCallHandler { call, result ->
                when (call.method) {
                    "sendDataToNative" -> {
                        val args = call.argument<String>("counter")
                        args?.let { _flutterMessage.value = it }
                        result.success(true)
                    }

                    else -> result.notImplemented()
                }
            }
            engine.navigationChannel.setInitialRoute("/")
            engine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

            FlutterEngineCache.getInstance().put(ENGINE_ID, engine)
        } catch (e: Throwable) {
            Timber.e(e, "Failed to warm up FlutterEngine")
        }
    }

    fun destroy() {
        _methodChannel?.setMethodCallHandler(null)
        _methodChannel = null
    }

    fun sendDataToFlutter(
        arguments: Any?,
        method: String = "updateFromNative",
    ) {
        if (_methodChannel == null) {
            Timber.w("MethodChannel is not ready")
            return
        }
        _methodChannel?.invokeMethod(method, arguments)
    }

    fun engine(): FlutterEngine? = FlutterEngineCache.getInstance().get(ENGINE_ID)

    /**
     * Gọi sau super.onDestroy() của AppFlutterActivity. Lúc đó surface của Activity đã được trả về
     * engine dùng chung và Dart đã nhận "detached" (Dart ngừng vẽ frame) — muộn hơn onResume của Home
     * — nên phải báo cho view nhúng tự nối lại surface và bật vẽ frame trở lại.
     */
    fun notifyFlutterScreenClosed() {
        _viewReattachSignal.value++
    }

    companion object {
        const val ENGINE_ID = "flutter_engine_id"
        private const val CHANNEL = "com.animations/flutter"

        fun isReady(): Boolean = FlutterEngineCache.getInstance().get(ENGINE_ID) != null
    }
}

fun Context.openFlutterScreen() {
    if (!FlutterEngineManager.isReady()) {
        Timber.w("FlutterEngine is not ready; cannot open the Flutter screen")
        return
    }

    val intent = FlutterActivity
        .CachedEngineIntentBuilder(
            AppFlutterActivity::class.java,
            FlutterEngineManager.ENGINE_ID
        )
        .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
        .build(this)

    startActivity(intent)
}
