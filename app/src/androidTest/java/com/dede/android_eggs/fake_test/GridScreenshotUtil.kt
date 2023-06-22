package com.dede.android_eggs.fake_test

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.InsetDrawable
import android.util.Size
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dede.android_eggs.R
import com.dede.android_eggs.fake_test.EasterEggsServer.Companion.registerHandler
import com.dede.android_eggs.ui.drawables.ScaleType
import com.dede.android_eggs.ui.drawables.ScaleTypeDrawable
import com.dede.basic.dpf
import com.dede.basic.requireDrawable
import com.wolt.blurhashkt.BlurHashDecoder
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.min

/**
 * Generate Grid Screenshot picture.
 *
 * @author shhu
 * @since 2023/6/19
 */
@Ignore("Generate Grid Screenshot picture") // remove this line to run test
@RunWith(AndroidJUnit4::class)
class GridScreenshotUtil {

    companion object {
        // Pixel 6
        private val TARGET_SIZE = Size(1080, 2400)

        private val GROUPS = listOf(
            Tetrad(810, false, 4),  // 4
            Split(360, 3),                // 3
            Triple(580, true, 4),   // 3
            Split(290, 4),                // 4
            Split(360, 2),                // 2
        )

        private const val ASSET_DIR = "screenshots"
    }

    interface Group {

        val height: Int

        fun convertBounds(width: Int, top: Int): List<Rect>
    }

    /**
     * --------------
     * |        |   |
     * |        |---|
     * |        |   |
     * --------------
     */
    private class Triple(
        override val height: Int,
        private val reverse: Boolean,
        private val column: Int = 3
    ) : Group {
        override fun convertBounds(width: Int, top: Int): List<Rect> {
            val halfHeight = height / 2
            return if (reverse) {
                val split = width / column
                listOf(
                    Rect(split, top, width, top + height),
                    Rect(0, top, split, top + halfHeight),
                    Rect(0, top + halfHeight, split, top + height),
                )
            } else {
                val split = (width * (column - 1) / column)
                listOf(
                    Rect(0, top, split, top + height),
                    Rect(split, top, width, top + halfHeight),
                    Rect(split, top + halfHeight, width, top + height),
                )
            }
        }
    }

    /**
     * -----
     * |   | ... column
     * -----
     */
    private class Split(override val height: Int, private val column: Int = 3) : Group {
        override fun convertBounds(width: Int, top: Int): List<Rect> {
            val w = width / column
            return (0 until column).map {
                Rect(w * it, top, w * (it + 1), top + height)
            }
        }
    }

    /**
     * ------------------
     * |            |   |
     * |            |---|
     * |            |   |
     * |            |---|
     * |            |   |
     * ------------------
     */
    private class Tetrad(
        override val height: Int,
        private val reverse: Boolean,
        private val column: Int = 4
    ) : Group {
        override fun convertBounds(width: Int, top: Int): List<Rect> {
            val splitH = height / 3
            return if (reverse) {
                val end = width / column
                listOf(
                    Rect(end, top, width, top + height),
                    Rect(0, top, end, top + splitH),
                    Rect(0, top + splitH, end, top + splitH * 2),
                    Rect(0, top + splitH * 2, end, top + height),
                )
            } else {
                val end = (width * (column - 1) / column)
                listOf(
                    Rect(0, top, end, top + height),
                    Rect(end, top, width, top + splitH),
                    Rect(end, top + splitH, width, top + splitH * 2),
                    Rect(end, top + splitH * 2, width, top + height),
                )
            }
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 2.dpf
        style = Paint.Style.STROKE
    }

    private fun cropScreenshot(
        context: Context,
        screenshot: String,
        targetWidth: Int = -1,
        targetHeight: Int = -1,
        drawBorder: Boolean = false,
    ): Bitmap {
        val delegate = context.assets.open("$ASSET_DIR/$screenshot").use {
            BitmapDrawable(context.resources, it)
        }
        var width = targetWidth
        var height = targetHeight
        if (width <= 0 || height <= 0) {
            val size = min(delegate.bitmap.width, delegate.bitmap.height)
            width = size
            height = size
        }
        val drawable = ScaleTypeDrawable(delegate, ScaleType.CENTER_CROP).apply {
            setBounds(0, 0, width, height)
        }
        return createBitmap(width, height).applyCanvas {
            drawable.draw(this)
            if (drawBorder) {
                drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
        }
    }

    private fun createGridScreenshot(context: Context, screenshots: List<String>): Bitmap {
        return createBitmap(TARGET_SIZE.width, TARGET_SIZE.height).applyCanvas {
            var top = 0
            var index = 0
            out@ for (group in GROUPS) {
                val bounds = group.convertBounds(TARGET_SIZE.width, top)
                for (rect in bounds) {
                    val snapshot = screenshots.getOrNull(index++) ?: break@out
                    val cropBitmap =
                        cropScreenshot(context, snapshot, rect.width(), rect.height(), true)
                    drawBitmap(cropBitmap, rect.left.toFloat(), rect.top.toFloat(), paint)
                }
                top += group.height
            }
        }
    }

    @Test
    fun generate() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val screenshots = requireNotNull(context.assets.list(ASSET_DIR)).reversed()
        EasterEggsServer.start(context) {
            for (screenshot in screenshots) {
                val name = File(screenshot).nameWithoutExtension
                registerHandler("/$name.jpeg") {
                    cropScreenshot(context, screenshot).toResponse()
                }
            }
            registerHandler("/u.jpeg") {
                val drawable = InsetDrawable(
                    context.requireDrawable(R.drawable.ic_android_udc),
                    0.15f
                )
                createBitmap(TARGET_SIZE.width, TARGET_SIZE.width).applyCanvas {
                    val blurhash = BlurHashDecoder.decode(
                        context.getString(R.string.hash_snapshot_bg),
                        TARGET_SIZE.width, TARGET_SIZE.height
                    )
                    if (blurhash != null) {
                        drawBitmap(blurhash, 0f, 0f, paint)
                    }
                    drawable.setBounds(0, 0, TARGET_SIZE.width, TARGET_SIZE.width)
                    drawable.draw(this)
                }.toResponse()
            }
            registerHandler("/ic_grid_screenshot.jpeg") {
                createGridScreenshot(context, screenshots).toResponse()
            }
        }
    }

    private fun Bitmap.toResponse(): Response {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, stream)
        recycle()
        val byteArray = stream.toByteArray()
        return NanoHTTPD.newFixedLengthResponse(
            Response.Status.OK, "image/jpeg",
            ByteArrayInputStream(byteArray),
            byteArray.size.toLong()
        )
    }
}