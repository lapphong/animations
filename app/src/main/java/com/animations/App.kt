package com.animations

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.animations.di.appModule
import com.animations.utils.FlutterEngineManager
import com.animations.utils.ShortcutsUtils
import com.animations.utils.settings.AppSettings
import com.animations.utils.settings.SharedPrefs
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application(), DefaultLifecycleObserver {
    override fun onCreate() {
        super<Application>.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }.koin
        AppSettings.init(getKoin().get<SharedPrefs>())
        ShortcutsUtils.createShortcut(this)
        FlutterEngineManager.init(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        FlutterEngineManager.destroy()
    }
}
