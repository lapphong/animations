package com.animations.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.animations.R
import com.animations.extensions.onClickNotRipple

@Composable
fun HeaderView(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int = R.drawable.ic_arrow_left,
    centerTitle: Boolean = true,
    onLeftClick: () -> Unit = {},
    rightContent: @Composable RowScope.() -> Unit = {},
) = HeaderView(
    title = stringResource(title),
    modifier = modifier,
    leftIcon = leftIcon,
    centerTitle = centerTitle,
    onLeftClick = onLeftClick,
    rightContent = rightContent,
)

@Composable
fun HeaderView(
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int = R.drawable.ic_arrow_left,
    centerTitle: Boolean = true,
    onLeftClick: () -> Unit = {},
    rightContent: @Composable RowScope.() -> Unit = {},
) = HeaderView(
    modifier = modifier,
    leftIcon = leftIcon,
    centerTitle = centerTitle,
    onLeftClick = onLeftClick,
    rightContent = rightContent,
) {
    GradientText(
        text = title,
        maxLines = 1,
        centerTitle = centerTitle,
        modifier = Modifier.weight(1f)
    )
}

@Composable
fun HeaderView(
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int = R.drawable.ic_arrow_left,
    centerTitle: Boolean = false,
    onLeftClick: () -> Unit = {},
    rightContent: @Composable RowScope.() -> Unit = {},
    titleContent: @Composable RowScope.() -> Unit,
) {
    val rootModifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(8.dp)
        .heightIn(min = 30.dp)

    if (centerTitle) {
        Box(modifier = rootModifier) {
            CenterRow(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
            ) {
                titleContent()
            }

            if (leftIcon != 0) {
                BackIcon(
                    res = leftIcon,
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onLeftClick,
                )
            }

            CenterRow(
                modifier = Modifier.align(Alignment.CenterEnd),
                itemSpacing = 12.dp,
            ) {
                rightContent()
            }
        }
    } else {
        CenterRow(
            modifier = rootModifier,
            itemSpacing = 8.dp,
        ) {
            if (leftIcon != 0) {
                BackIcon(res = leftIcon, onClick = onLeftClick)
            }

            titleContent()

            CenterRow(itemSpacing = 12.dp) {
                rightContent()
            }
        }
    }
}

@Composable
private fun BackIcon(
    @DrawableRes res: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    AppIcon(
        res = res,
        color = Color(0xFF1E88E5),
        modifier = modifier
            .size(24.dp)
            .onClickNotRipple("BtnBack") { onClick() }
    )
}

@Composable
fun HeaderViewUninstall(
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int = R.drawable.ic_arrow_left,
    centerTitle: Boolean = false,
    onLeftClick: () -> Unit = {},
    rightContent: @Composable RowScope.() -> Unit = {},
    titleContent: @Composable RowScope.() -> Unit,
) {
    val rootModifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(start = 16.dp, top = 12.dp, end = 16.dp)
        .heightIn(min = 30.dp)

    if (centerTitle) {
        Box(modifier = rootModifier) {
            CenterRow(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp),
            ) {
                titleContent()
            }

            if (leftIcon != 0) {
                BackIcon(
                    res = leftIcon,
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onLeftClick,
                )
            }

            CenterRow(
                modifier = Modifier.align(Alignment.CenterEnd),
                itemSpacing = 12.dp,
            ) {
                rightContent()
            }
        }
    } else {
        CenterRow(
            modifier = rootModifier,
            itemSpacing = 8.dp,
        ) {
            if (leftIcon != 0) {
                BackIcon(res = leftIcon, onClick = onLeftClick)
            }

            titleContent()

            CenterRow(itemSpacing = 12.dp) {
                rightContent()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHeaderView() {
    HeaderView(
        title = R.string.app_name,
        onLeftClick = {},
    )
}
