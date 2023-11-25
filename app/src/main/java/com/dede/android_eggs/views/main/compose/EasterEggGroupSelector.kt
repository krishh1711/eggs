package com.dede.android_eggs.views.main.compose

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dede.android_eggs.main.EasterEggHelp
import com.dede.android_eggs.main.EggActionHelp
import com.dede.android_eggs.util.getActivity
import com.dede.basic.provider.BaseEasterEgg
import com.dede.basic.provider.EasterEggGroup
import kotlin.math.roundToInt


@Composable
fun Modifier.withEasterEggGroupSelector(
    base: BaseEasterEgg,
    onSelected: (index: Int) -> Unit,
): Modifier {
    if (base !is EasterEggGroup) {
        return this
    }

    // todo support shape https://issuetracker.google.com/issues/200529605
//    val context = LocalContext.current
//    var expanded by remember { mutableStateOf(false) }
//    DropdownMenu(
//        expanded = expanded,
//        onDismissRequest = {
//            expanded = false
//        },
//        modifier = Modifier.clip(shapes.extraLarge),
//    ) {
//        base.eggs.forEachIndexed { index, egg ->
//            val menuTitle = EasterEggHelp.VersionFormatter.create(egg.apiLevel, egg.nicknameRes)
//                .format(context)
//            DropdownMenuItem(
//                leadingIcon = {
//                    EasterEggLogo(egg = egg, 30.dp)
//                },
//                text = {
//                    Text(
//                        text = menuTitle,
//                        style = typography.bodyLarge,
//                    )
//                },
//                onClick = {
//                    onSelected.invoke(index)
//                    expanded = false
//                }
//            )
//        }
//    }
//    return clickable {
//        expanded = true
//    }

    val activity: Activity? = LocalContext.current.getActivity()
    var popupAnchorBounds by remember { mutableStateOf(Rect.Zero) }
    return clickable {
        // DropdownMenu style error, use native popup
        if (activity == null || popupAnchorBounds.isEmpty) return@clickable
        val parent = activity.findViewById<FrameLayout>(android.R.id.content)
        val fakeView = View(activity).apply {
            x = popupAnchorBounds.left
            y = popupAnchorBounds.top
        }
        val params = FrameLayout.LayoutParams(
            popupAnchorBounds.width.roundToInt(),
            popupAnchorBounds.height.roundToInt()
        )
        parent.addView(fakeView, params)
        EggActionHelp.showEggGroupMenu(
            activity,
            fakeView,
            base,
            onSelected = onSelected,
            onDismiss = {
                parent.removeView(fakeView)
            })
    }.onGloballyPositioned {
        val position = it.positionInWindow()
        val bounds = it.boundsInParent()
        popupAnchorBounds = Rect(
            position.x,
            position.y,
            position.x + bounds.width,
            position.y + bounds.height
        )
    }
}
