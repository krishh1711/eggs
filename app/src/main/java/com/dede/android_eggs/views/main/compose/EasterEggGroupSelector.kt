package com.dede.android_eggs.views.main.compose

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.dede.android_eggs.views.main.util.EasterEggHelp
import com.dede.basic.provider.BaseEasterEgg
import com.dede.basic.provider.EasterEggGroup


@Composable
fun Modifier.withEasterEggGroupSelector(
    base: BaseEasterEgg,
    onSelected: (index: Int) -> Unit,
): Modifier {
    if (base !is EasterEggGroup) {
        return this
    }

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        },
        shape = shapes.extraLarge,
        offset = DpOffset(x = (-12).dp, y = 12.dp),
    ) {
        base.eggs.forEachIndexed { index, egg ->
            val menuTitle = EasterEggHelp.VersionFormatter.create(egg.apiLevelRange, egg.nicknameRes)
                .format(context)
            DropdownMenuItem(
                leadingIcon = {
                    EasterEggLogo(egg = egg, 30.dp)
                },
                text = {
                    Text(
                        text = menuTitle,
                        style = typography.bodyLarge,
                    )
                },
                onClick = {
                    onSelected.invoke(index)
                    expanded = false
                }
            )
        }
    }
    return clickable {
        expanded = true
    }
}
