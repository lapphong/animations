package com.animations.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.animations.R

@Composable
fun AppIcon(
    modifier: Modifier,
    @DrawableRes res: Int,
    color: Color = Color.Black
) {
    Icon(
        modifier = modifier,
        painter = painterResource(res),
        contentDescription = null,
        tint = color
    )
}

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    @DrawableRes res: Int,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
) {
    Image(
        modifier = modifier,
        painter = painterResource(res),
        alignment = alignment,
        contentDescription = null,
        contentScale = contentScale
    )
}

//@Composable
//fun AppAsyncImage(
//    modifier: Modifier = Modifier,
//    data: Any?,
//    size: Int? = null,
//    contentScale: ContentScale = ContentScale.Crop,
//    onImageSizeChanged: (Size) -> Unit = {},
//) {
//    val context = LocalContext.current
//    val request = remember(data, size) {
//        ImageRequest.Builder(context)
//            .data(data)
//            .crossfade(true)
//            .apply {
//                if (size != null) size(size)
//                if (data != null) memoryCacheKey(data.toString())
//            }
//
//            .build()
//    }
//    var isLoaded by remember(data, size) { mutableStateOf(false) }
//
//    Box(modifier = modifier) {
//        if (!isLoaded) SimpleShimmer(modifier = Modifier.matchParentSize())
//
//        AsyncImage(
//            model = request,
//            contentDescription = null,
//            contentScale = contentScale,
//            modifier = Modifier.fillMaxSize(),
//            onState = { state ->
//                isLoaded = state is AsyncImagePainter.State.Success
//                if (state is AsyncImagePainter.State.Success) {
//                    onImageSizeChanged(state.painter.intrinsicSize)
//                }
//            },
//        )
//    }
//}

@Composable
fun AppLottie(
    modifier: Modifier = Modifier,
    @RawRes res: Int,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    reverseOnRepeat: Boolean = true,
    speed: Float = 1f,
    iterations: Int = LottieConstants.IterateForever,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(res))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = restartOnPlay,
        reverseOnRepeat = reverseOnRepeat,
        iterations = iterations,
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        contentScale = contentScale,
        progress = { progress },
    )
}

@Preview(showBackground = true)
@Composable
private fun AppLottiePreview() {
    AppLottie(
        modifier = Modifier.size(120.dp),
        res = R.raw.lottie_click,
    )
}
