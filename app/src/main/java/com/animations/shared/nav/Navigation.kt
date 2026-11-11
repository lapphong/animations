package com.animations.shared.nav

import androidx.navigation3.runtime.NavKey
import timber.log.Timber

fun <T : NavKey> MutableList<T>.navigateTo(
    dest: T,
    popupTos: List<Class<out T>> = emptyList(),
) {
    if (lastOrNull() != dest) {
        Timber.d("navigateTo: $dest")
        add(dest)
        if (popupTos.isNotEmpty()) {
            removeIf { it !== dest && it::class.java in popupTos }
        }
    } else {
        Timber.d("navigateTo: skip ${lastOrNull()}")
    }
}

fun MutableList<NavKey>.pop() {
    if (size > 1) removeLastOrNull()
}

fun MutableList<NavKey>.pop(dest: Class<out NavKey>) {
    Timber.d("pop: $size - $dest")
    if (size > 1) {
        removeIf { it::class.java == dest }
    } else {
        Timber.d("pop: skip")
    }
}

fun MutableList<NavKey>.pops(list: List<Class<out NavKey>>) {
    if (size > 1) {
        if (size > list.size) {
            removeIf { it::class.java in list }
        } else {
            removeAll(takeLast(size - 1))
        }
    } else {
        Timber.d("popAllInClass: skip")
    }
}
