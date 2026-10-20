package com.animations

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.SystemClock
import android.view.KeyEvent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.animations.models.OpenTo
import com.animations.utils.AppFlutterActivity
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlutterScreenTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    @Test
    fun shortcutOpensFullScreenFlutter() {
        closeFlutterScreenIfOpen()
        launchShortcut()

        assertTrue(
            "Shortcut did not open AppFlutterActivity within 30s",
            waitForResumedActivity(AppFlutterActivity::class.java, 30_000),
        )
    }

    /**
     * Home must handle rotation itself. A recreated Home builds a second FlutterView for the shared
     * engine, which leaves the full-screen Flutter screen without a surface.
     */
    @Test
    fun homeHandlesRotationWithoutRecreation() {
        val context = instrumentation.targetContext
        val activityInfo = context.packageManager.getActivityInfo(
            ComponentName(context, MainActivity::class.java),
            0,
        )
        val handled = ActivityInfo.CONFIG_ORIENTATION or ActivityInfo.CONFIG_SCREEN_SIZE or
            ActivityInfo.CONFIG_SCREEN_LAYOUT or ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE

        assertTrue(
            "MainActivity must declare configChanges for orientation, screenSize, screenLayout and smallestScreenSize",
            activityInfo.configChanges and handled == handled,
        )
    }

    private fun launchShortcut() {
        val context = instrumentation.targetContext
        context.startActivity(
            Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(OpenTo.KEY, OpenTo.OPEN_TO_FLUTTER_SCREEN.name)
            }
        )
    }

    private fun closeFlutterScreenIfOpen() {
        repeat(2) {
            if (resumedActivity() !is AppFlutterActivity) return
            instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
            Thread.sleep(1_000)
        }
    }

    private fun waitForResumedActivity(clazz: Class<out Activity>, timeoutMs: Long): Boolean {
        val deadline = SystemClock.uptimeMillis() + timeoutMs
        while (SystemClock.uptimeMillis() < deadline) {
            if (clazz.isInstance(resumedActivity())) return true
            Thread.sleep(200)
        }
        return false
    }

    private fun resumedActivity(): Activity? {
        var activity: Activity? = null
        instrumentation.runOnMainSync {
            activity = ActivityLifecycleMonitorRegistry.getInstance()
                .getActivitiesInStage(Stage.RESUMED)
                .firstOrNull()
        }
        return activity
    }
}
