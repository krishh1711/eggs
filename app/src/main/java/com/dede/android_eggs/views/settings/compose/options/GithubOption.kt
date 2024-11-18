package com.dede.android_eggs.views.settings.compose.options

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.dede.android_eggs.R
import com.dede.android_eggs.ui.composes.icons.Github
import com.dede.android_eggs.util.CustomTabsBrowser
import com.dede.android_eggs.views.settings.compose.basic.Option
import com.dede.android_eggs.views.settings.compose.basic.imageVectorIconBlock

@Composable
fun GithubOption() {
    val context = LocalContext.current
    Option(
        leadingIcon = imageVectorIconBlock(
            imageVector = Icons.Github,
            contentDescription = stringResource(R.string.label_github)
        ),
        title = stringResource(R.string.label_github),
        desc = stringResource(R.string.url_github),
        trailingContent = imageVectorIconBlock(imageVector = Icons.Rounded.Star),
        onClick = {
            com.dede.android_eggs.util.CustomTabsBrowser.launchUrl(context, R.string.url_github)
        }
    )
}
