package com.carousel_slider

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.carousel_slider.extensions.cardOverlay
import com.carousel_slider.extensions.cardTapGesture
import com.carousel_slider.extensions.cardTransform
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CarouselSlider(
    modifier: Modifier = Modifier,
    items: List<String>,
    options: CarouselOptions = CarouselDefaults.Options,
    onItemClick: (index: Int) -> Unit = {},
) {
    val count = items.size
    val loopCount = Int.MAX_VALUE
    val startPage = remember(count) { (loopCount / 2).let { it - it % count } }
    val state = rememberPagerState(initialPage = startPage, pageCount = { loopCount })
    val scope = rememberCoroutineScope()
    var lastInteractionTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var scrollJob by remember { mutableStateOf<Job?>(null) }

    fun animateToPage(page: Int) {
        scrollJob?.cancel()
        scrollJob = scope.launch { state.animateScrollToPage(page) }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .collect { scrolling ->
                if (scrolling) {
                    lastInteractionTime = System.currentTimeMillis()
                }
            }
    }

    LaunchedEffect(state, options.autoPlay) {
        if (!options.autoPlay) return@LaunchedEffect

        while (true) {
            delay(options.autoPlayInterval)

            val idle = System.currentTimeMillis() - lastInteractionTime
            if (idle >= 2000L && !state.isScrollInProgress) {
                animateToPage(state.currentPage + 1)
                lastInteractionTime = System.currentTimeMillis()
            }
        }
    }

    BoxWithConstraints(modifier.fillMaxSize()) {
        val side = ((maxWidth - options.cardSize) / 2).coerceAtLeast(0.dp)
        val halfCardPx = with(LocalDensity.current) { options.cardSize.toPx() / 2f }

        HorizontalPager(
            state = state,
            pageSize = PageSize.Fixed(options.cardSize),
            contentPadding = PaddingValues(horizontal = side),
            pageSpacing = options.cardSpacing,
            modifier = Modifier
                .fillMaxSize()
                .cardTapGesture(
                    halfCardPx,
                    onPrevious = {
                        animateToPage(state.currentPage - 1)
                    }, onNext = {
                        animateToPage(state.currentPage + 1)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) { page ->
            val index = page % count

            Column(
                modifier = Modifier
                    .zIndex(-abs(state.currentPage - page).toFloat())
                    .graphicsLayer {
                        val offset = (state.currentPage - page) + state.currentPageOffsetFraction
                        cardTransform(offset)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier
                        .width(options.cardSize)
                        .height(options.cardHeight)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            lastInteractionTime = System.currentTimeMillis()
                            if (page == state.settledPage) {
                                onItemClick(index)
                            } else {
                                animateToPage(page)
                            }
                        }
                ) {
                    GlideImage(
                        model = items[index],
                        contentDescription = "CarouselSlider ${index + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .cardOverlay(state, page),
                    )
                }

                if (options.reflection) {
                    Spacer(Modifier.height(6.dp))
                    Box(
                        Modifier
                            .width(options.cardSize)
                            .height(options.cardHeight * 0.5f)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        GlideImage(
                            model = items[index],
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleY = -1f
                                    alpha = 0.35f
                                },
                            contentScale = ContentScale.Crop,
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        0f to Color.Transparent,
                                        1f to options.background,
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}
