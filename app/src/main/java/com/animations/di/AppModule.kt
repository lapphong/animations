package com.animations.di

import com.animations.utils.FlutterEngineManager
import com.animations.utils.settings.SharedPrefs
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::SharedPrefs)
    singleOf(::FlutterEngineManager)
}
