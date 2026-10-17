package com.animations.models

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
