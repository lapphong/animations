package com.animations.nav

import androidx.navigation3.runtime.NavKey
import com.animations.models.OpenTo
import kotlinx.serialization.Serializable

@Serializable
data object SplashKey : NavKey

@Serializable
data object HomeKey : NavKey

@Serializable
data object DisintegrationDemoKey : NavKey

@Serializable
data class CarouselSliderDemoKey(val title: String) : NavKey

fun OpenTo.navKey(): NavKey? = when (this) {
    OpenTo.OPEN_TO_DISINTEGRATION -> DisintegrationDemoKey
    OpenTo.OPEN_TO_CAROUSEL_SLIDER -> CarouselSliderDemoKey("From shortcut")
    OpenTo.OPEN_TO_FLUTTER_SCREEN -> DisintegrationDemoKey
    OpenTo.OPEN_TO_DEFAULT -> null
}
