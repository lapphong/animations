package com.animations.di

import com.animations.utils.settings.SharedPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { SharedPrefs(androidContext()) }
}
