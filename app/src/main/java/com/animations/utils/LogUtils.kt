package com.animations.utils

import timber.log.Timber

object LogUtils {
    fun d(ex: String) {
        Timber.d(ex)
    }

    fun d(ex: Throwable) {
        Timber.d(ex)
    }

    fun e(ex: String) {
        Timber.e(ex)
    }

    fun e(ex: Throwable) {
        Timber.e(ex)
    }
}
