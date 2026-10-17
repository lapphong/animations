package com.animations.models

import android.content.Context
import androidx.navigation3.runtime.NavKey
import com.animations.R
import com.animations.shared.nav.CarouselSliderDemoKey
import com.animations.shared.nav.DisintegrationDemoKey

enum class OpenTo {
    OPEN_TO_DEFAULT,
    OPEN_TO_DISINTEGRATION,
    OPEN_TO_CAROUSEL_SLIDER,
    OPEN_TO_FLUTTER_SCREEN;

    companion object {
        const val KEY = "KEY_OPEN_TO"

        fun of(value: String?): OpenTo =
            entries.firstOrNull { it.name == value } ?: OPEN_TO_DEFAULT
    }
}

fun OpenTo.navKey(context: Context): NavKey? = when (this) {
    OpenTo.OPEN_TO_DISINTEGRATION -> DisintegrationDemoKey
    OpenTo.OPEN_TO_CAROUSEL_SLIDER -> CarouselSliderDemoKey(context.getString(R.string.from_shortcut))
    OpenTo.OPEN_TO_FLUTTER_SCREEN -> DisintegrationDemoKey
    OpenTo.OPEN_TO_DEFAULT -> null
}
