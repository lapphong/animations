package com.animations.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.animations.extensions.shimmerEffect

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    text: String = "Custom Gradient Button",
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .heightIn(min = 48.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF1E88E5), Color(0xFF1565C0)),
                    start = Offset.Zero,
                    end = Offset(1000f, 1000f)
                ),
                shape = RoundedCornerShape(15.dp)
            )
            .shimmerEffect()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = text,
            fontSize = 18.sp,
            color = Color.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomGradientButtonPreview() {
    GradientButton(
        modifier = Modifier.padding(10.dp),
        onClick = { },
    )
}
