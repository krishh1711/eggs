package com.dede.android_eggs.routes

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dede.android_eggs.R
import com.dede.android_eggs.presist.PersistMapCleanup
import com.dede.android_eggs.routes.styling.homeScreenTabIndexKey
import com.dede.android_eggs.routes.styling.rememberPreference
import com.dede.android_eggs.routing.RouteHandler
import com.dede.android_eggs.routing.defaultStacking
import com.dede.android_eggs.routing.defaultStill
import com.dede.android_eggs.routing.defaultUnstacking
import com.dede.android_eggs.routing.isStacking
import com.dede.android_eggs.routing.isUnknown
import com.dede.android_eggs.routing.isUnstacking
import com.dede.android_eggs.views.main.compose.EasterEggScreen
import com.dede.android_eggs.views.main.util.EasterEggHelp
import com.dede.basic.provider.BaseEasterEgg


private const val HIGHEST_COUNT = 1

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
@Preview(showBackground = true)
fun EasterEggScreenPager(
    easterEggs: List<BaseEasterEgg> = EasterEggHelp.previewEasterEggs(),
    searchFilter: String = "",
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {


    val saveableStateHolder = rememberSaveableStateHolder()

    PersistMapCleanup("home/")
    RouteHandler(
        listenToGlobalEmitter = true,
        transitionSpec = {
            when {
                isStacking -> defaultStacking
                isUnstacking -> defaultUnstacking
                isUnknown -> when {
                    initialState.route == searchRoute && targetState.route == searchResultRoute -> defaultStacking
                    initialState.route == searchResultRoute && targetState.route == searchRoute -> defaultUnstacking
                    else -> defaultStill
                }

                else -> defaultStill
            }
        }
    ) {
        globalRoutes()
        host {
            val (tabIndex, onTabChanged) = rememberPreference(
                homeScreenTabIndexKey,
                defaultValue = 0
            )

            Scaffold(
                topIconButtonId = R.drawable.ic_android_classic,
                onTopIconButtonClick = { settingsRoute() },
                tabIndex = tabIndex,
                onTabChanged = onTabChanged,
                tabColumnContent = { Item ->
                    Item(0, "Quick picks", R.drawable.ic_pgyer_logo)
                    Item(1, "Songs", R.drawable.ic_android_classic)
                    Item(2, "Playlists", R.drawable.ic_compose_logo)
                    Item(3, "Artists", R.drawable.ic_compose_logo)
                    Item(4, "Albums", R.drawable.ic_compose_logo)
                }
            ) { currentTabIndex ->
                saveableStateHolder.SaveableStateProvider(key = currentTabIndex) {
                    when (currentTabIndex) {
                        0 -> EasterEggScreen(easterEggs)

                        1 -> EasterEggScreen(easterEggs)

                        2 -> EasterEggScreen(easterEggs)
                        3 -> EasterEggScreen(easterEggs)

                        4 -> EasterEggScreen(easterEggs)
                    }
                }
            }
        }
    }
    /*
        val context = LocalContext.current
        val pureEasterEggs = remember(easterEggs) {
            EasterEggModules.providePureEasterEggList(easterEggs)
        }
        val searchText = remember(searchFilter) {
            searchFilter.trim().uppercase()
        }
        val searchMode = searchText.isNotBlank()
        val currentList = remember(searchText, searchMode, easterEggs, pureEasterEggs) {
            if (searchMode) {
                filterEasterEggs(context, pureEasterEggs, searchText)
            } else {
                easterEggs
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Crossfade(
                targetState = currentList.isEmpty(),
                modifier = Modifier.sizeIn(maxWidth = 560.dp),
                label = "EasterEggList",
            ) { isEmpty ->
                if (isEmpty) {
                    SearchEmpty(contentPadding)
                } else {
                    LazyColumn(
                        contentPadding = contentPadding + PaddingValues(vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (searchMode) {
                            items(items = currentList) {
                                EasterEggItem(it, enableItemAnim = true)
                            }
                        } else {
                            val highestList = currentList.subList(0, HIGHEST_COUNT)
                            val normalList = currentList.subList(HIGHEST_COUNT, currentList.size)
                            items(items = highestList) {
                                EasterEggHighestItem(it)
                            }
                            item {
                                Wavy(res = R.drawable.ic_wavy_line)
                            }
    //                        items(items = normalList) {
    //                            EasterEggItem(it, enableItemAnim = false)
    //                        }
                            item("wavy2") {
                                Wavy(res = R.drawable.ic_wavy_line)
                            }
                            item("footer") {
                                ProjectDescription()
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchEmpty(contentPadding: PaddingValues) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(top = 32.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(108.dp)
            )
        }
    }

    private fun filterEasterEggs(
        context: Context,
        pureEasterEggs: List<EasterEgg>,
        searchText: String,
    ): List<EasterEgg> {
        val isApiLevel = Regex("^\\d{1,2}$").matches(searchText)

        fun EasterEgg.matchVersionName(version: String): Boolean {
            val containsStart = EasterEggHelp.getVersionNameByApiLevel(apiLevelRange.first)
                .contains(version, true)
            return if (apiLevelRange.first == apiLevelRange.last) {
                containsStart
            } else {
                containsStart || EasterEggHelp.getVersionNameByApiLevel(apiLevelRange.last)
                    .contains(version, true)
            }
        }
        return pureEasterEggs.filter {
            context.getString(it.nameRes).contains(searchText, true) ||
                    context.getString(it.nicknameRes).contains(searchText, true) ||
                    it.matchVersionName(searchText) ||
                    (isApiLevel && it.apiLevelRange.contains(searchText.toIntOrNull() ?: -1))
        }*/
}
