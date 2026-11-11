package com.animations.models

import android.content.Context
import com.animations.R
import com.animations.shared.nav.Dest

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

fun OpenTo.dest(context: Context): Dest? = when (this) {
    OpenTo.OPEN_TO_DISINTEGRATION -> Dest.Disintegration
    OpenTo.OPEN_TO_CAROUSEL_SLIDER -> Dest.CarouselSlider(context.getString(R.string.from_shortcut))
    OpenTo.OPEN_TO_FLUTTER_SCREEN -> null
    OpenTo.OPEN_TO_DEFAULT -> null
}
