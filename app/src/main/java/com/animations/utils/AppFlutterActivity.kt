package com.animations.utils

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.animations.ui.theme.primary
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.renderer.FlutterUiDisplayListener
import org.koin.android.ext.android.inject

class AppFlutterActivity : FlutterActivity() {
    private val engineManager: FlutterEngineManager by inject()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSpinnerUntilFirstFrame()
    }

    override fun onResume() {
        super.onResume()
        // Re-enable Flutter back handling in case the initial PopScope signal was missed.
        setFrameworkHandlesBack(true)
    }

    override fun onPostResume() {
        super.onPostResume()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
    }

    override fun onDestroy() {
        super.onDestroy()
        // Notify when the user closes the Flutter screen, not when the Activity is destroyed in the background.
        if (isFinishing) engineManager.notifyFlutterScreenClosed()
    }

    private fun showSpinnerUntilFirstFrame() {
        val flutterView = findViewById<FlutterView>(FLUTTER_VIEW_ID)
        if (flutterView.hasRenderedFirstFrame()) return

        val progress = ProgressBar(this).apply {
            indeterminateTintList = ColorStateList.valueOf(primary.toArgb())
        }
        addContentView(
            progress,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )

        flutterView.addOnFirstFrameRenderedListener(
            object : FlutterUiDisplayListener {
                override fun onFlutterUiDisplayed() {
                    (progress.parent as? ViewGroup)?.removeView(progress)
                }

                override fun onFlutterUiNoLongerDisplayed() = Unit
            }
        )
    }
}
