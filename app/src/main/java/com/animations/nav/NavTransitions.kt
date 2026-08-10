package com.animations.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

private const val TRANSITION_DURATION = 500

enum class SlideDirection {
    LEFT,
    RIGHT,
    UP,
    DOWN
}

object NavTransitions {
    /**
     * Slide destination in from the given direction.
     */
    fun slideTransition(
        direction: SlideDirection = SlideDirection.RIGHT
    ) = NavDisplay.transitionSpec { enterTransition(direction) } +
            NavDisplay.popTransitionSpec { exitTransition(direction) } +
            NavDisplay.predictivePopTransitionSpec { predictiveExitTransition(direction) }
}

fun enterTransition(
    direction: SlideDirection
) = when (direction) {
    SlideDirection.LEFT -> slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.RIGHT -> slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.UP -> slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.DOWN -> slideInVertically(
        initialOffsetY = { -it },
        animationSpec = tween(TRANSITION_DURATION)
    )
} togetherWith ExitTransition.None

fun exitTransition(
    direction: SlideDirection
) = EnterTransition.None togetherWith when (direction) {
    SlideDirection.LEFT -> slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.RIGHT -> slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.UP -> slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.DOWN -> slideOutVertically(
        targetOffsetY = { -it },
        animationSpec = tween(TRANSITION_DURATION)
    )
}

fun predictiveExitTransition(
    direction: SlideDirection
) = EnterTransition.None togetherWith when (direction) {
    SlideDirection.LEFT -> slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.RIGHT -> slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.UP -> slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(TRANSITION_DURATION)
    )

    SlideDirection.DOWN -> slideOutVertically(
        targetOffsetY = { -it },
        animationSpec = tween(TRANSITION_DURATION)
    )
}
