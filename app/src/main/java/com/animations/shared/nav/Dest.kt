package com.animations.shared.nav

import android.os.Parcelable
import androidx.navigation3.runtime.NavKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
sealed class Dest : NavKey, Parcelable {
    @Parcelize
    @Serializable
    data object Splash : Dest()

    @Parcelize
    @Serializable
    data object Home : Dest()

    @Parcelize
    @Serializable
    data object Disintegration : Dest()

    @Parcelize
    @Serializable
    data class CarouselSlider(val title: String) : Dest()
}
