package com.animations.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.os.bundleOf
import com.animations.MainActivity
import com.animations.R
import com.animations.models.OpenTo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ShortcutsUtils {
    private const val ID_DISINTEGRATION = "shortcut_disintegration"
    private const val ID_CAROUSEL_SLIDER = "shortcut_carousel_slider"
    private const val ID_FLUTTER_SCREEN = "shortcut_flutter_screen"

    fun createShortcut(context: Context) {
        val handler = CoroutineExceptionHandler { _, e ->
            LogUtils.e("createShortcut failed: ${e.toString()}")
        }
        CoroutineScope(Dispatchers.IO + handler).launch {
            val shortcuts = listOf(
                buildShortcut(
                    context = context,
                    id = ID_DISINTEGRATION,
                    label = context.getString(R.string.disintegration),
                    intent = shortcutIntent(context, OpenTo.OPEN_TO_DISINTEGRATION),
                    shortcutIcon = R.drawable.ic_shortcut_disintegration,
                    rank = 0,
                ),
                buildShortcut(
                    context = context,
                    id = ID_CAROUSEL_SLIDER,
                    label = context.getString(R.string.carousel_slider),
                    intent = shortcutIntent(context, OpenTo.OPEN_TO_CAROUSEL_SLIDER),
                    shortcutIcon = R.drawable.ic_shortcut_carousel_slider,
                    rank = 1,
                ),
                buildShortcut(
                    context = context,
                    id = ID_FLUTTER_SCREEN,
                    label = context.getString(R.string.flutter_screen),
                    intent = shortcutIntent(context, OpenTo.OPEN_TO_FLUTTER_SCREEN),
                    shortcutIcon = R.drawable.ic_shortcut_flutter,
                    rank = 2,
                ),
            )
            ShortcutManagerCompat.setDynamicShortcuts(context, shortcuts)
            LogUtils.d("createShortcut: ${shortcuts.size} shortcut")
        }
    }

    private fun shortcutIntent(context: Context, openTo: OpenTo): Intent =
        Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtras(bundleOf(OpenTo.KEY to openTo.name))
        }

    private fun buildShortcut(
        context: Context,
        id: String,
        label: String,
        intent: Intent,
        shortcutIcon: Int,
        rank: Int,
    ): ShortcutInfoCompat = ShortcutInfoCompat.Builder(context, id)
        .setShortLabel(label)
        .setLongLabel(label)
        .setIntent(intent)
        .setIcon(IconCompat.createWithResource(context, shortcutIcon))
        .setRank(rank)
        .build()
}
