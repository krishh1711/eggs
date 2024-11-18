package com.dede.android_eggs.views.settings.compose.groups

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.dede.android_eggs.R
import com.dede.android_eggs.util.CustomTabsBrowser
import com.dede.android_eggs.views.settings.compose.basic.ExpandOptionsPref
import com.dede.android_eggs.views.settings.compose.basic.Option
import com.dede.android_eggs.views.settings.compose.basic.imageVectorIconBlock
import com.dede.android_eggs.views.settings.compose.options.GithubOption
import com.dede.android_eggs.views.settings.compose.options.VersionOption

@Preview
@Composable
fun AboutGroup() {
    val context = LocalContext.current

    ExpandOptionsPref(
        leadingIcon = Icons.Rounded.Info,
        title = stringResource(R.string.label_about)
    ) {
        VersionOption()
        Option(
            leadingIcon = {
                Text(
                    "β",
                    fontSize = 5.2.em,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(24.dp),
                )
            },
            title = stringResource(R.string.label_beta),
            trailingContent = imageVectorIconBlock(imageVector = Icons.Rounded.Download),
            onClick = {
                com.dede.android_eggs.util.CustomTabsBrowser.launchUrl(context, R.string.url_beta)
            }
        )

        GithubOption()
        Option(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Policy,
                contentDescription = stringResource(R.string.label_privacy_policy),
            ),
            title = stringResource(R.string.label_privacy_policy),
            onClick = {
                com.dede.android_eggs.util.CustomTabsBrowser.launchUrl(context, R.string.url_privacy)
            }
        )
    }
}
