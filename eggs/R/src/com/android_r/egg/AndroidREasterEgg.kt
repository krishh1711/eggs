package com.android_r.egg

import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import com.android_r.egg.neko.NekoControlsService
import com.dede.basic.provider.BaseEasterEgg
import com.dede.basic.provider.ComponentProvider
import com.dede.basic.provider.EasterEgg
import com.dede.basic.provider.EasterEggProvider
import com.dede.basic.provider.TimelineEvent
import com.dede.basic.provider.TimelineEvent.Companion.timelineEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AndroidREasterEgg : EasterEggProvider, ComponentProvider {

    @Provides
    @IntoSet
    @Singleton
    override fun provideEasterEgg(): BaseEasterEgg {
        return object : EasterEgg(
            iconRes = R.drawable.r_icon,
            nameRes = R.string.r_egg_name,
            nicknameRes = R.string.r_android_nickname,
            apiLevel = Build.VERSION_CODES.R,
            actionClass = PlatLogoActivity::class.java
        ) {
            override fun provideSnapshotProvider(): SnapshotProvider {
                return SnapshotProvider()
            }
        }
    }

    @Provides
    @IntoSet
    @Singleton
    override fun provideTimelineEvents(): List<TimelineEvent> {
        return listOf(
            timelineEvent(
                Build.VERSION_CODES.R,
                "R.\nReleased publicly as Android 11 in September 2020."
            )
        )
    }

    @Provides
    @IntoSet
    @Singleton
    override fun provideComponent(): ComponentProvider.Component {
        return object : ComponentProvider.Component(
            iconRes = R.drawable.r_ic_fullcat_icon,
            nameRes = R.string.r_egg_name,
            nicknameRes = R.string.r_android_nickname,
            apiLevel = Build.VERSION_CODES.R
        ) {
            @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
            override fun isSupported(): Boolean {
                return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
            }

            @RequiresApi(Build.VERSION_CODES.R)
            override fun isEnabled(context: Context): Boolean {
                val cn = ComponentName(context, NekoControlsService::class.java)
                return cn.isEnabled(context)
            }

            @RequiresApi(Build.VERSION_CODES.R)
            override fun setEnabled(context: Context, enable: Boolean) {
                val cn = ComponentName(context, NekoControlsService::class.java)
                cn.setEnable(context, enable)
            }
        }
    }
}