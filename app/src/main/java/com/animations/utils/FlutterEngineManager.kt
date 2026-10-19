package com.animations.utils

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

object FlutterEngineManager {
    const val ENGINE_ID = "flutter_engine_id"

    private const val CHANNEL = "com.animations/flutter"

    private val _viewReattachSignal = MutableStateFlow(0)

    /** Increments whenever the embedded FlutterView needs to be reattached after a full-screen Flutter Activity closes. */
    val viewReattachSignal: StateFlow<Int> = _viewReattachSignal.asStateFlow()

    private var _methodChannel: MethodChannel? = null

    private var resultListener: ((String) -> Unit)? = null

    fun setResultListener(listener: (String) -> Unit) {
        resultListener = listener
    }

    fun init(context: Context) {
        if (engine() != null) {
            return
        }

        try {
            val engine = FlutterEngine(context.applicationContext)
            _methodChannel = MethodChannel(engine.dartExecutor.binaryMessenger, CHANNEL)
            _methodChannel?.setMethodCallHandler { call, result ->
                when (call.method) {
                    "sendDataToNative" -> {
                        val data = call.argument<String>("counter")
                        if (data != null) {
                            resultListener?.invoke(data)
                        }
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

    fun sendDataToFlutter(data: String) {
        if (_methodChannel == null) {
            Timber.w("MethodChannel is not ready")
            return
        }
        _methodChannel?.invokeMethod("updateFromNative", data)
    }

    fun isReady(): Boolean = engine() != null

    fun engine(): FlutterEngine? = FlutterEngineCache.getInstance().get(ENGINE_ID)

    /**
     * Gọi sau super.onDestroy() của AppFlutterActivity. Lúc đó surface của Activity đã được trả về
     * engine dùng chung và Dart đã nhận "detached" (Dart ngừng vẽ frame) — muộn hơn onResume của Home
     * — nên phải báo cho view nhúng tự nối lại surface và bật vẽ frame trở lại.
     */
    fun notifyFlutterScreenClosed() {
        _viewReattachSignal.value++
    }
}

fun Context.openFlutterScreen(
    onResult: ((String) -> Unit)? = null,
) {
    if (!FlutterEngineManager.isReady()) {
        return
    }
    onResult?.let { FlutterEngineManager.setResultListener(onResult) }

    val intent = FlutterActivity
        .CachedEngineIntentBuilder(
            AppFlutterActivity::class.java,
            FlutterEngineManager.ENGINE_ID
        )
        .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
        .build(this)

    startActivity(intent)
}
