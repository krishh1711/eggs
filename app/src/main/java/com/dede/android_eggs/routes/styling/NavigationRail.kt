package com.dede.android_eggs.routes.styling

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dede.android_eggs.util.isLandscape

@Composable
inline fun NavigationRail(
    topIconButtonId: Int,
    noinline onTopIconButtonClick: () -> Unit,
    tabIndex: Int,
    crossinline onTabIndexChanged: (Int) -> Unit,
    content: @Composable (@Composable (Int, String, Int) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val (colorPalette, typography) = LocalAppearance.current
    val isLandscape = isLandscape
    val transition = updateTransition(targetState = tabIndex, label = null)

    val paddingValues = PaddingValues(0.dp)
    Row(
        modifier = modifier
            .background(colorScheme.background)
    ) {

        if (isLandscape) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                content { index, text, icon ->
                    val dothAlpha by transition.animateFloat(label = "") {
                        if (it == index) 1f else 1f
                    }
                    val textColor by transition.animateColor(label = "") {
                        if (it == index) colorPalette.accent else colorPalette.text
                    }

                    val iconContent: @Composable () -> Unit = {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier
                                .graphicsLayer {
                                    alpha = dothAlpha
                                    translationX = (1f - dothAlpha) * -48.dp.toPx()
                                    rotationZ = if (isLandscape) 0f else -90f
                                }
                                .size(Dimensions.navigationRailIconOffset * 4)
                        )
                    }

                    val textContent: @Composable () -> Unit = {
                        BasicText(
                            text = text,
                            style = typography.xs.semiBold.center.color(textColor),
                            modifier = Modifier
                                .vertical(enabled = !isLandscape)
                                .rotate(if (isLandscape) 0f else -90f)
                                .padding(horizontal = 16.dp)
                        )
                    }

                    val contentModifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .clickable(onClick = { onTabIndexChanged(index) })
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = contentModifier
                            .padding(top = 8.dp)
                    ) {
                        iconContent()
                        textContent()
                    }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp)
            ) {
                content { index, text, icon ->
                    val dothAlpha by transition.animateFloat(label = "") {
                        if (it == index) 1f else 1f
                    }
                    val textColor by transition.animateColor(label = "") {
                        if (it == index) colorPalette.accent else colorPalette.text
                    }

                    val iconContent: @Composable () -> Unit = {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier
                                .vertical(enabled = !isLandscape)
                                .graphicsLayer {
                                    alpha = dothAlpha
                                    translationX = (1f - dothAlpha) * -48.dp.toPx()
                                    rotationZ = if (isLandscape) 0f else 0f
                                }
                                .size(Dimensions.navigationRailIconOffset * 4)
                        )
                    }

                    val textContent: @Composable () -> Unit = {
                        BasicText(
                            text = text,
                            style = typography.xs.semiBold.center.color(textColor),
                            modifier = Modifier
                                .vertical(enabled = !isLandscape)
                                .rotate(if (isLandscape) 0f else -90f)
                                .padding(horizontal = 16.dp)
                        )
                    }

                    val contentModifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .clickable(onClick = { onTabIndexChanged(index) })
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = contentModifier
                            .padding(top = 8.dp)
                    ) {
                        iconContent()
                        textContent()
                    }
                }
            }
        }
    }
}

fun Modifier.vertical(enabled: Boolean = true) =
    if (enabled)
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
            layout(placeable.height, placeable.width) {
                placeable.place(
                    x = -(placeable.width / 2 - placeable.height / 2),
                    y = -(placeable.height / 2 - placeable.width / 2)
                )
            }
        } else this
