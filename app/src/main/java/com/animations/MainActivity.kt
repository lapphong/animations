package com.animations

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
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.animations.nav.CarouselSliderDemoKey
import com.animations.nav.DisintegrationDemoKey
import com.animations.nav.HomeKey
import com.animations.nav.NavTransitions
import com.animations.nav.Navigator
import com.animations.nav.SlideDirection
import com.animations.nav.enterTransition
import com.animations.nav.exitTransition
import com.animations.nav.predictiveExitTransition
import com.animations.nav.rememberNavigationState
import com.animations.nav.toEntries
import com.animations.ui.CarouselSliderDemo
import com.animations.ui.DisintegrationDemo
import com.animations.ui.HomeScreen
import com.animations.ui.theme.AnimationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationsTheme {
                AnimationApp()
            }
        }
    }
}

@Composable
fun AnimationApp() {
    val navigationState = rememberNavigationState(
        startKey = HomeKey,
        topLevelKeys = setOf(HomeKey),
    )
    val navigator = remember { Navigator(navigationState) }
    val entryProvider = entryProvider {
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
