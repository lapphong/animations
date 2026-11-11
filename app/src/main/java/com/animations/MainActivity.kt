package com.animations

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.animations.models.OpenTo
import com.animations.models.dest
import com.animations.shared.nav.Dest
import com.animations.shared.nav.NavTransitions
import com.animations.shared.nav.SlideDirection
import com.animations.shared.nav.enterTransition
import com.animations.shared.nav.exitTransition
import com.animations.shared.nav.navigateTo
import com.animations.shared.nav.pop
import com.animations.shared.nav.predictiveExitTransition
import com.animations.ui.CarouselSliderDemo
import com.animations.ui.DisintegrationDemo
import com.animations.ui.HomeScreen
import com.animations.ui.SplashScreen
import com.animations.ui.theme.AnimationsTheme
import com.animations.utils.settings.AppSettings
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) initArgs()
        setContent {
            AnimationsTheme {
                AnimationApp()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        initArgs()
    }

    private fun initArgs() {
        AppSettings.openTo = OpenTo.OPEN_TO_DEFAULT.name
        val extras = intent.extras
        if (extras != null) {
            AppSettings.openTo = OpenTo.of(extras.getString(OpenTo.KEY)).name
        }
        Timber.d("initArgs: openTo=${AppSettings.openTo}")
    }
}

@Composable
fun AnimationApp() {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(Dest.Splash)

    val entryProvider = entryProvider<NavKey> {
        entry<Dest.Splash> {
            SplashScreen(
                onFinished = {
                    val openTo = OpenTo.of(AppSettings.openTo)
                    if (openTo != OpenTo.OPEN_TO_FLUTTER_SCREEN) {
                        AppSettings.openTo = OpenTo.OPEN_TO_DEFAULT.name
                    }
                    backStack.navigateTo(Dest.Home, listOf(Dest.Splash::class.java))
                    openTo.dest(context)?.let { backStack.navigateTo(it) }
                }
            )
        }
        entry<Dest.Home> {
            HomeScreen(onNavigate = { backStack.navigateTo(it) })
        }
        entry<Dest.Disintegration>(
            metadata = NavTransitions.slideTransition(SlideDirection.DOWN)
        ) {
            DisintegrationDemo(
                goToCarouselSlider = { backStack.navigateTo(Dest.CarouselSlider(it)) },
                onBackPressed = { backStack.pop() }
            )
        }
        entry<Dest.CarouselSlider>(
            metadata = NavTransitions.slideTransition(SlideDirection.UP)
        ) { args ->
            CarouselSliderDemo(
                title = args.title,
                onBackPressed = { backStack.pop() }
            )
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            transitionSpec = {
                enterTransition(SlideDirection.RIGHT)
            },
            popTransitionSpec = {
                exitTransition(SlideDirection.RIGHT)
            },
            predictivePopTransitionSpec = {
                predictiveExitTransition(SlideDirection.RIGHT)
            },
            entryProvider = entryProvider,
        )
    }
}
