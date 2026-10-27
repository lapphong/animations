package com.animations

import android.app.Application
import com.animations.di.appModule
import com.animations.utils.ShortcutsUtils
import com.animations.utils.settings.AppSettings
import com.animations.utils.settings.SharedPrefs
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            setupStrictMode()
        }
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        AppSettings.init(getKoin().get<SharedPrefs>())
        ShortcutsUtils.createShortcut(this)
    }

    private fun setupStrictMode() {
//        StrictMode.setThreadPolicy(
//            StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .penaltyDialog()
//                .build()
//        )
//
//        StrictMode.setVmPolicy(
//            StrictMode.VmPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .build()
//        )
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}
