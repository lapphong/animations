package com.animations

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.animations.utils.FlutterEngineManager

class App : Application(), DefaultLifecycleObserver {
    override fun onCreate() {
        super<Application>.onCreate()
        FlutterEngineManager.init(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        FlutterEngineManager.destroy()
    }
}
