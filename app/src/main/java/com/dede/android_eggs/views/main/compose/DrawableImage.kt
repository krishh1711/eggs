package com.dede.android_eggs.views.main.compose

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dede.android_eggs.R
import com.dede.android_eggs.ui.drawables.AlterableAdaptiveIconDrawable
import com.dede.basic.requireDrawable
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
@Preview(showBackground = true)
private fun PreviewImage() {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DrawableImage(res = R.mipmap.ic_launcher_round, contentDescription = null)

        val maskPath = stringArrayResource(id = R.array.icon_shape_override_paths).last()
        val drawable = remember(context.theme, maskPath) {
            AlterableAdaptiveIconDrawable(context, R.mipmap.ic_launcher, maskPath)
        }
        DrawableImage(
            drawable = drawable, contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
    }
}

@Composable
fun DrawableImage(
    @DrawableRes res: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val context = LocalContext.current
    val drawable = remember(res, context.theme) {
        context.requireDrawable(res)
    }
    DrawableImage(
        drawable = drawable,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
    )
}

@Composable
fun DrawableImage(
    drawable: Drawable,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val drawablePainter = rememberDrawablePainter(drawable = drawable)
    Image(
        painter = drawablePainter,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
    )
}
