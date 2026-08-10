package com.animations.widgets

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.animations.extensions.rememberAnimatedGradient

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    val animatedGradient = rememberAnimatedGradient()

    Text(
        text = text,
        modifier = modifier.onSizeChanged { animatedGradient.size = it },
        style = textStyle.copy(brush = animatedGradient.brush()),
        fontWeight = FontWeight.W500,
    )
}

@Preview(showBackground = true)
@Composable
fun GradientTextPreview() {
    GradientText(text = "GradientText")
}
