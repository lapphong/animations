package com.animations

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.animations.models.OpenTo
import com.animations.models.navKey
import com.animations.shared.nav.CarouselSliderDemoKey
import com.animations.shared.nav.DisintegrationDemoKey
import com.animations.shared.nav.HomeKey
import com.animations.shared.nav.NavTransitions
import com.animations.shared.nav.Navigator
import com.animations.shared.nav.SlideDirection
import com.animations.shared.nav.SplashKey
import com.animations.shared.nav.enterTransition
import com.animations.shared.nav.exitTransition
import com.animations.shared.nav.predictiveExitTransition
import com.animations.shared.nav.rememberNavigationState
import com.animations.shared.nav.toEntries
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

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
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
    val navigationState = rememberNavigationState(
        initialKey = SplashKey,
        rootKey = HomeKey,
        topLevelKeys = setOf(HomeKey),
    )
    val navigator = remember { Navigator(navigationState) }
    val entryProvider = entryProvider {
        entry<SplashKey> {
            SplashScreen(
                onFinished = {
                    val openTo = OpenTo.of(AppSettings.openTo).navKey(context)
                    navigator.goBack()
                    openTo?.let(navigator::navigate)
                }
            )
        }
        entry<HomeKey> {
            HomeScreen(onNavigate = navigator::navigate)
        }
        entry<DisintegrationDemoKey>(
            metadata = NavTransitions.slideTransition(SlideDirection.DOWN)
        ) {
            DisintegrationDemo(
                goToCarouselSlider = { navigator.navigate(CarouselSliderDemoKey(it)) },
                onBackPressed = { navigator.goBack() }
            )
        }
        entry<CarouselSliderDemoKey>(
            metadata = NavTransitions.slideTransition(SlideDirection.UP)
        ) { args ->
            CarouselSliderDemo(
                title = args.title,
                onBackPressed = { navigator.goBack() }
            )
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            entries = navigationState.toEntries(entryProvider),
            onBack = navigator::goBack,
            transitionSpec = {
                enterTransition(SlideDirection.RIGHT)
            },
            popTransitionSpec = {
                exitTransition(SlideDirection.RIGHT)
            },
            predictivePopTransitionSpec = {
                predictiveExitTransition(SlideDirection.RIGHT)
            }
        )
    }
}
