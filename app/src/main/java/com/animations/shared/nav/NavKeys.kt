package com.animations.shared.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object SplashKey : NavKey

@Serializable
data object HomeKey : NavKey

@Serializable
data object DisintegrationDemoKey : NavKey

@Serializable
data class CarouselSliderDemoKey(val title: String) : NavKey
