package com.dede.android_eggs.routes


//import it.vfsfitvnm.compose.routing.Route0
//import it.vfsfitvnm.compose.routing.Route1
//import it.vfsfitvnm.compose.routing.RouteHandlerScope

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.dede.android_eggs.routing.RouteHandlerScope
import com.dede.android_eggs.routing.Route0
import com.dede.android_eggs.routing.Route1
val albumRoute = Route1<String?>("albumRoute")
val artistRoute = Route1<String?>("artistRoute")
val localPlaylistRoute = Route1<Long?>("localPlaylistRoute")
val playlistRoute = Route1<String?>("playlistRoute")
val searchResultRoute = Route1<String>("searchResultRoute")
val searchRoute = Route1<String>("searchRoute")
val settingsRoute = Route0("settingsRoute")

@SuppressLint("ComposableNaming")
@Suppress("NOTHING_TO_INLINE")
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
inline fun RouteHandlerScope.globalRoutes() {
//    settingsRoute()

//    artistRoute { browseId ->
//        PagerSettingsScreen(
//            browseId = browseId ?: error("browseId cannot be null")
//        )
//    }
//
//    playlistRoute { browseId ->
//        PagerSettingsScreen(
//            browseId = browseId ?: error("browseId cannot be null")
//        )
//    }
}
