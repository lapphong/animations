package com.animations.utils

import android.content.Context
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

object FlutterEngineManager {
    const val ENGINE_ID = "flutter_engine_id"
    private const val TAG = "FlutterEngineManager"

    fun init(context: Context) {
        if (FlutterEngineCache.getInstance().get(ENGINE_ID) != null) {
            return
        }

        try {
            val engine = FlutterEngine(context.applicationContext)

            engine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

            FlutterEngineCache.getInstance().put(ENGINE_ID, engine)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to warm up FlutterEngine", e)
        }
    }

    fun isReady(): Boolean = FlutterEngineCache.getInstance().get(ENGINE_ID) != null
}

fun Context.openFlutterScreen() {
    if (!FlutterEngineManager.isReady()) {
        return
    }

    val intent = FlutterActivity
        .withCachedEngine(FlutterEngineManager.ENGINE_ID)
        .build(this)

    startActivity(intent)
}
