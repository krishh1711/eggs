package com.android_next.egg

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dede.basic.provider.BaseEasterEgg
import com.dede.basic.provider.EasterEgg
import com.dede.basic.provider.EasterEggProvider
import com.dede.basic.provider.SnapshotProvider
import com.dede.basic.provider.TimelineEvent
import com.dede.basic.requireDrawable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import java.util.Calendar
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
object AndroidNextEasterEgg : EasterEggProvider {

    const val RELEASE_YEAR = 2025
    private const val RELEASE_MONTH = Calendar.SEPTEMBER

    private const val NEXT_API = Build.VERSION_CODES.CUR_DEVELOPMENT// android next

    private const val TIMELINE_EVENT = "Wow, Android Next."

    @StringRes
    private val NICKNAME_RES = R.string.nickname_android_next

    @DrawableRes
    private val LOGO_RES = R.drawable.ic_droid_logo

    @DrawableRes
    private val PLATLOGO_RES = R.drawable.img_droid_next

    fun getTimelineMessage(context: Context): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        return if (year > RELEASE_YEAR) {
            context.getString(R.string.summary_android_release_pushed)
        } else {
            context.getString(R.string.summary_android_waiting)
        }
    }

//    @Provides
//    @IntoSet
//    @Singleton
    override fun provideEasterEgg(): BaseEasterEgg {
        return object : EasterEgg(
            iconRes = LOGO_RES,
            nameRes = NICKNAME_RES,
            nicknameRes = NICKNAME_RES,
            apiLevel = NEXT_API,
        ) {
            override fun onEasterEggAction(context: Context): Boolean {
                androidNextDialogVisible = true
                return true
            }

            override fun provideSnapshotProvider(): SnapshotProvider {
                return object : SnapshotProvider() {
                    override fun create(context: Context): View {
                        return ImageView(context).apply {
                            setImageDrawable(context.requireDrawable(PLATLOGO_RES))
                        }
                    }

                    override val insertPadding: Boolean = false
                }
            }

        }
    }

//    @Provides
//    @IntoSet
//    @Singleton
    override fun provideTimelineEvents(): List<TimelineEvent> {
        return listOf(
            TimelineEvent(
                year = RELEASE_YEAR,
                month = RELEASE_MONTH,
                apiLevel = NEXT_API,
                event = TIMELINE_EVENT
            )
        )
    }
}
