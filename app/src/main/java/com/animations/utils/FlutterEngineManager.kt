package com.animations.utils

import android.content.Context
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

object FlutterEngineManager {
    const val ENGINE_ID = "flutter_engine_id"
    private const val TAG = "FlutterEngineManager"

    private const val CHANNEL = "com.animations/flutter"

    private var _methodChannel: MethodChannel? = null

    private var resultListener: ((String) -> Unit)? = null

    fun setResultListener(listener: (String) -> Unit) {
        resultListener = listener
    }

    fun init(context: Context) {
        if (FlutterEngineCache.getInstance().get(ENGINE_ID) != null) {
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
            Log.e(TAG, "Failed to warm up FlutterEngine", e)
        }
    }

    fun destroy() {
        _methodChannel?.setMethodCallHandler(null)
        _methodChannel = null
    }

    fun sendDataToFlutter(data: String) {
        if (_methodChannel == null) {
            Log.w(TAG, "MethodChannel is not ready")
            return
        }
        _methodChannel?.invokeMethod("updateFromNative", data)
    }

    fun isReady(): Boolean = FlutterEngineCache.getInstance().get(ENGINE_ID) != null
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
        .build(this)

    startActivity(intent)
}
