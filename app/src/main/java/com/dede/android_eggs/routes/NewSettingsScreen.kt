package com.dede.android_eggs.routes

//import com.dede.android_eggs.routing.RouteHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dede.android_eggs.R
import com.dede.android_eggs.routing.RouteHandler
import com.dede.android_eggs.routes.styling.LocalAppearance
import com.dede.android_eggs.routes.styling.color
import com.dede.android_eggs.routes.styling.secondary
import com.dede.android_eggs.routes.styling.semiBold
import com.dede.android_eggs.views.main.util.EasterEggHelp
import com.dede.basic.provider.BaseEasterEgg


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun NewSettingsScreen(easterEgg: List<BaseEasterEgg> = EasterEggHelp.previewEasterEggs()) {
    val saveableStateHolder = rememberSaveableStateHolder()

    val (tabIndex, onTabChanged) = rememberSaveable {
        mutableStateOf(0)
    }

    RouteHandler(listenToGlobalEmitter = true) {
        globalRoutes()

        host {
            Scaffold(
                topIconButtonId = R.drawable.img_android_studio,
                onTopIconButtonClick = pop,
                tabIndex = tabIndex,
                onTabChanged = onTabChanged,
                tabColumnContent = { Item ->
                    Item(0, "Appearance", R.drawable.ic_pgyer_logo)
                    Item(1, "Player", R.drawable.ic_pgyer_logo)
                    Item(2, "Cache", R.drawable.better_together_hero)
                    Item(3, "Database", R.drawable.img_android_ai_tools_hero)
                    Item(4, "Other", R.drawable.better_together_hero)
                    Item(5, "About", R.drawable.better_together_hero)
                    Item(6, "About", R.drawable.better_together_hero)
                    Item(7, "About", R.drawable.better_together_hero)
                    Item(9, "About", R.drawable.better_together_hero)
                    Item(8, "About", R.drawable.better_together_hero)
                    Item(10, "About", R.drawable.better_together_hero)
                    Item(11, "About", R.drawable.better_together_hero)
                }
            ) { currentTabIndex ->
                saveableStateHolder.SaveableStateProvider(currentTabIndex) {
                    when (currentTabIndex) {
                        0 -> PagerSettingsScreen(easterEgg)
                        1 -> PagerSettingsScreen(easterEgg)
                        2 -> PagerSettingsScreen(easterEgg)
                        3 -> PagerSettingsScreen(easterEgg)
                        4 -> PagerSettingsScreen(easterEgg)
                        5 -> PagerSettingsScreen(easterEgg)
                        6 -> PagerSettingsScreen(easterEgg)
                        7 -> PagerSettingsScreen(easterEgg)
                        9 -> PagerSettingsScreen(easterEgg)
                        8 -> PagerSettingsScreen(easterEgg)
                        10 -> PagerSettingsScreen(easterEgg)
                        11 -> PagerSettingsScreen(easterEgg)
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : Enum<T>> EnumValueSelectorSettingsEntry(
    title: String,
    selectedValue: T,
    crossinline onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    crossinline valueText: (T) -> String = Enum<T>::name,
    noinline trailingContent: (@Composable () -> Unit)? = null
) {
    ValueSelectorSettingsEntry(
        title = title,
        selectedValue = selectedValue,
        values = enumValues<T>().toList(),
        onValueSelected = onValueSelected,
        modifier = modifier,
        isEnabled = isEnabled,
        valueText = valueText,
        trailingContent = trailingContent,
    )
}

@Composable
inline fun <T> ValueSelectorSettingsEntry(
    title: String,
    selectedValue: T,
    values: List<T>,
    crossinline onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    crossinline valueText: (T) -> String = { it.toString() },
    noinline trailingContent: (@Composable () -> Unit)? = null
) {
    var isShowingDialog by remember {
        mutableStateOf(false)
    }

//    if (isShowingDialog) {
//        ValueSelectorDialog(
//            onDismiss = { isShowingDialog = false },
//            title = title,
//            selectedValue = selectedValue,
//            values = values,
//            onValueSelected = onValueSelected,
//            valueText = valueText
//        )
//    }

    SettingsEntry(
        title = title,
        text = valueText(selectedValue),
        modifier = modifier,
        isEnabled = isEnabled,
        onClick = { isShowingDialog = true },
        trailingContent = trailingContent
    )
}

@Composable
fun SwitchSettingEntry(
    title: String,
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    SettingsEntry(
        title = title,
        text = text,
        isEnabled = isEnabled,
        onClick = { onCheckedChange(!isChecked) },
        trailingContent = {},
        modifier = modifier
    )
}

@Composable
fun SettingsEntry(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    trailingContent: (@Composable () -> Unit)? = null
) {
    val (colorPalette, typography) = LocalAppearance.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(enabled = isEnabled, onClick = onClick)
            .alpha(if (isEnabled) 1f else 0.5f)
            .padding(start = 16.dp)
            .padding(all = 16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            BasicText(
                text = title,
                style = typography.xs.semiBold.copy(color = colorPalette.text),
            )

            BasicText(
                text = text,
                style = typography.xs.semiBold.copy(color = colorPalette.textSecondary),
            )
        }

        trailingContent?.invoke()
    }
}

@Composable
fun SettingsDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    val (_, typography) = LocalAppearance.current

    BasicText(
        text = text,
        style = typography.xxs.secondary,
        modifier = modifier
            .padding(start = 16.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun ImportantSettingsDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    val (colorPalette, typography) = LocalAppearance.current

    BasicText(
        text = text,
        style = typography.xxs.semiBold.color(colorPalette.red),
        modifier = modifier
            .padding(start = 16.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingsEntryGroupText(
    title: String,
    modifier: Modifier = Modifier,
) {
    val (colorPalette, typography) = LocalAppearance.current

    BasicText(
        text = title.uppercase(),
        style = typography.xxs.semiBold.copy(colorPalette.accent),
        modifier = modifier
            .padding(start = 16.dp)
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun SettingsGroupSpacer(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .height(24.dp)
    )
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
@Preview
fun settpreview() {
    NewSettingsScreen(easterEgg = EasterEggHelp.previewEasterEggs())
}