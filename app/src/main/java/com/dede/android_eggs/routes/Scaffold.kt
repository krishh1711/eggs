package com.dede.android_eggs.routes


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dede.android_eggs.routes.styling.LocalAppearance
import com.dede.android_eggs.routes.styling.NavigationRail


@ExperimentalAnimationApi
@Composable
fun Scaffold(
    topIconButtonId: Int,
    onTopIconButtonClick: () -> Unit,
    tabIndex: Int,
    onTabChanged: (Int) -> Unit,
    tabColumnContent: @Composable RowScope.(@Composable (Int, String, Int) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.(Int) -> Unit
) {
    val (colorPalette) = LocalAppearance.current

    Column(
        modifier = modifier
            .background(colorPalette.background0)
            .fillMaxSize()
    ) {
        AnimatedContent(
            targetState = tabIndex,
            modifier = Modifier
                .fillMaxHeight(0.85F)
                .fillMaxWidth(),
            transitionSpec = {
                val slideDirection = when (targetState > initialState) {
                    true -> AnimatedContentTransitionScope.SlideDirection.Up
                    false -> AnimatedContentTransitionScope.SlideDirection.Down
                }

                val animationSpec = spring(
                    dampingRatio = 0.9f,
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = IntOffset.VisibilityThreshold
                )

                slideIntoContainer(slideDirection, animationSpec) with
                        slideOutOfContainer(slideDirection, animationSpec)
            },
            content = content, label = ""
        )
        NavigationRail(
            topIconButtonId = topIconButtonId,
            onTopIconButtonClick = onTopIconButtonClick,
            tabIndex = tabIndex,
            onTabIndexChanged = onTabChanged,
            content = tabColumnContent
        )
    }
}



