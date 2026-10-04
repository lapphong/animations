package com.animations.widgets

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.animations.extensions.rememberAnimatedGradient

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    centerTitle: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    val animatedGradient = rememberAnimatedGradient()

    Text(
        text = text,
        modifier = modifier.onSizeChanged { animatedGradient.size = it },
        style = textStyle.copy(brush = animatedGradient.brush()),
        fontWeight = FontWeight.W500,
        textAlign = if (centerTitle) TextAlign.Center else TextAlign.Start,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

@Preview(showBackground = true)
@Composable
fun GradientTextPreview() {
    GradientText(text = "GradientText")
}
