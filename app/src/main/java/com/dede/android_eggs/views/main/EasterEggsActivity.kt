@file:OptIn(ExperimentalMaterial3Api::class)

package com.dede.android_eggs.views.main

import android.annotation.SuppressLint
import android.app.assist.AssistContent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import com.android_next.egg.AndroidNextTimelineDialog
import com.dede.android_eggs.R
import com.dede.android_eggs.inject.FlavorFeatures
import com.dede.android_eggs.routes.EasterEggScreenPager
import com.dede.android_eggs.routes.styling.Appearance
import com.dede.android_eggs.routes.styling.Dimensions
import com.dede.android_eggs.routes.styling.LocalAppearance
import com.dede.android_eggs.routes.styling.applyFontPaddingKey
import com.dede.android_eggs.routes.styling.colorPaletteModeKey
import com.dede.android_eggs.routes.styling.colorPaletteNameKey
import com.dede.android_eggs.routes.styling.colorPaletteOf
import com.dede.android_eggs.routes.styling.enums.ThumbnailRoundness
import com.dede.android_eggs.routes.styling.getEnum
import com.dede.android_eggs.routes.styling.preferences
import com.dede.android_eggs.routes.styling.rememberBottomSheetState
import com.dede.android_eggs.routes.styling.thumbnailRoundnessKey
import com.dede.android_eggs.routes.styling.typographyOf
import com.dede.android_eggs.routes.styling.useSystemFontKey
import com.dede.android_eggs.ui.composes.ReverseModalNavigationDrawer
import com.dede.android_eggs.util.LocalEvent
import com.dede.android_eggs.util.OrientationAngleSensor
import com.dede.android_eggs.util.ThemeUtils
import com.dede.android_eggs.util.compose.end
import com.dede.android_eggs.views.main.compose.AnimatorDisabledAlertDialog
import com.dede.android_eggs.views.main.compose.BottomSearchBar
import com.dede.android_eggs.views.main.compose.Konfetti
import com.dede.android_eggs.views.main.compose.LocalEasterEggLogoSensor
import com.dede.android_eggs.views.main.compose.LocalFragmentManager
import com.dede.android_eggs.views.main.compose.LocalKonfettiState
import com.dede.android_eggs.views.main.compose.MainTitleBar
import com.dede.android_eggs.views.main.compose.Welcome
import com.dede.android_eggs.views.main.compose.rememberBottomSearchBarState
import com.dede.android_eggs.views.main.compose.rememberKonfettiState
import com.dede.android_eggs.views.main.util.EasterEggLogoSensorMatrixConvert
import com.dede.android_eggs.views.main.util.EasterEggShortcutsHelp
import com.dede.android_eggs.views.main.util.IntentHandler
import com.dede.android_eggs.views.settings.SettingsScreen
import com.dede.android_eggs.views.settings.compose.basic.SettingPrefUtil
import com.dede.android_eggs.views.settings.compose.prefs.IconVisualEffectsPrefUtil
import com.dede.android_eggs.views.theme.AppTheme
import com.dede.basic.Utils
import com.dede.basic.provider.BaseEasterEgg
import com.dede.basic.provider.EasterEgg
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@AndroidEntryPoint
class EasterEggsActivity : AppCompatActivity() {

    @Inject
    lateinit var easterEggs: List<@JvmSuppressWildcards BaseEasterEgg>

    @Inject
    lateinit var pureEasterEggs: List<@JvmSuppressWildcards EasterEgg>

    @Inject
    @ActivityScoped
    lateinit var intentHandler: IntentHandler

    private var orientationAngleSensor: OrientationAngleSensor? = null

    @Inject
    lateinit var sensor: EasterEggLogoSensorMatrixConvert

    @SuppressLint("UnusedBoxWithConstraintsScope")
    @OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class,
        ExperimentalMaterial3Api::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.tryApplyOLEDTheme(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val konfettiState = rememberKonfettiState()
            val coroutineScope = rememberCoroutineScope()
            val isSystemInDarkTheme = isSystemInDarkTheme()

            var appearance by rememberSaveable(
                isSystemInDarkTheme,
                stateSaver = Appearance.Companion
            ) {
                with(preferences) {
                    val colorPaletteName = getEnum(
                        colorPaletteNameKey,
                        com.dede.android_eggs.routes.styling.ColorPaletteName.Dynamic
                    )
                    val colorPaletteMode = getEnum(
                        colorPaletteModeKey,
                        com.dede.android_eggs.routes.styling.ColorPaletteMode.System
                    )
                    val thumbnailRoundness =
                        getEnum(thumbnailRoundnessKey, ThumbnailRoundness.Light)

                    val useSystemFont = getBoolean(useSystemFontKey, false)
                    val applyFontPadding = getBoolean(applyFontPaddingKey, false)

                    val colorPalette =
                        colorPaletteOf(colorPaletteName, colorPaletteMode, isSystemInDarkTheme)

                    setSystemBarAppearance(colorPalette.isDark)

                    mutableStateOf(
                        Appearance(
                            colorPalette = colorPalette,
                            typography = typographyOf(
                                colorPalette.text,
                                useSystemFont,
                                applyFontPadding
                            ),
                            thumbnailShape = thumbnailRoundness.shape()
                        )
                    )
                }
            }
            val density = LocalDensity.current
            val windowsInsets = WindowInsets.systemBars
            val bottomDp = with(density) { windowsInsets.getBottom(density).toDp() }

            val playerBottomSheetState = rememberBottomSheetState(
                dismissedBound = 0.dp,
                collapsedBound = Dimensions.collapsedPlayer + bottomDp,
                expandedBound = 100.dp
            )

            val playerAwareWindowInsets by remember(bottomDp, playerBottomSheetState.value) {
                derivedStateOf {
                    val bottom = playerBottomSheetState.value.coerceIn(
                        bottomDp,
                        playerBottomSheetState.collapsedBound
                    )

                    windowsInsets
                        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                        .add(WindowInsets(bottom = bottom))
                }
            }


            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(appearance.colorPalette.background0)
            ) {
                CompositionLocalProvider(
                    LocalAppearance provides appearance,
                    LocalFragmentManager provides supportFragmentManager,
                    LocalEasterEggLogoSensor provides sensor,
                    LocalKonfettiState provides konfettiState,
                    LocalPlayerAwareWindowInsets provides playerAwareWindowInsets,
                ) {
                    AppTheme {
                        val drawerState = rememberDrawerState(DrawerValue.Closed)
                        ReverseModalNavigationDrawer(
                            drawerContent = {
                                ModalDrawerSheet(
                                    drawerState = drawerState,
                                    drawerShape = shapes.extraLarge.end(0.dp),
                                    windowInsets = WindowInsets(0, 0, 0, 0)
                                ) {
                                    val maxWidth =
                                        LocalConfiguration.current.smallestScreenWidthDp * 0.8f
                                    Box(modifier = Modifier.width(maxWidth.dp)) {
                                        SettingsScreen(drawerState)
                                    }
                                }
                            },
                            drawerState = drawerState
                        ) {
                            val searchBarState = rememberBottomSearchBarState()
                            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                            Scaffold(
                                topBar = {
                                    MainTitleBar(
                                        scrollBehavior = scrollBehavior,
                                        searchBarState = searchBarState,
                                        drawerState = drawerState,
                                    )
                                },
                                modifier = Modifier
                                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                                bottomBar = {
                                    BottomSearchBar(searchBarState)
                                }
                            ) { contentPadding ->
//                                EasterEggScreen(
                                    EasterEggScreenPager(
                                    easterEggs,
                                    searchBarState.searchText,
                                    contentPadding
                                )


                            }
                        }

                        val context = LocalContext.current
                        val animatorDisabledAlertState = remember { mutableStateOf(false) }

                        Welcome(onNext = {
                            if (!Utils.areAnimatorEnabled(context)) {
                                animatorDisabledAlertState.value = true
                            }
                        })

                        AnimatorDisabledAlertDialog(animatorDisabledAlertState)

                        Konfetti(konfettiState)

                        AndroidNextTimelineDialog()
                    }


                }
            }
        }

        handleOrientationAngleSensor(IconVisualEffectsPrefUtil.isEnable(this))
        LocalEvent.receiver(this).register(IconVisualEffectsPrefUtil.ACTION_CHANGED)
        {
            val enable = it.getBooleanExtra(SettingPrefUtil.EXTRA_VALUE, false)
            handleOrientationAngleSensor(enable)
        }

        intentHandler.handleIntent(intent)
        EasterEggShortcutsHelp.updateShortcuts(this, pureEasterEggs)

        // call flavor features
        FlavorFeatures.get().call(this)
    }


    private fun handleOrientationAngleSensor(enable: Boolean) {
        val orientationAngleSensor = this.orientationAngleSensor
        if (enable && orientationAngleSensor == null) {
            this.orientationAngleSensor = OrientationAngleSensor(
                this, this, sensor
            )
        } else if (!enable && orientationAngleSensor != null) {
            orientationAngleSensor.destroy()
            this.orientationAngleSensor = null
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentHandler.handleIntent(intent)
    }

    override fun onProvideAssistContent(outContent: AssistContent?) {
        super.onProvideAssistContent(outContent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && outContent != null) {
            outContent.webUri = getString(R.string.url_github).toUri()
        }
    }

    private fun setSystemBarAppearance(isDark: Boolean) {
        with(WindowCompat.getInsetsController(window, window.decorView.rootView)) {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }

//        if (!isAtLeastAndroid6) {
//            window.statusBarColor =
//                (if (isDark) Color.Transparent else Color.Black.copy(alpha = 0.2f)).toArgb()
//        }
//
//        if (!isAtLeastAndroid8) {
//            window.navigationBarColor =
//                (if (isDark) Color.Transparent else Color.Black.copy(alpha = 0.2f)).toArgb()
//        }
    }
}
