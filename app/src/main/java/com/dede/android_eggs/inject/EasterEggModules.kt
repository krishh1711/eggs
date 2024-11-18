package com.dede.android_eggs.inject

import com.android_b.egg.AndroidBaseEasterEgg
//import com.android_g.egg.AndroidGingerbreadEasterEgg
//import com.android_h.egg.AndroidHoneycombEasterEgg
//import com.android_i.egg.AndroidIceCreamSandwichEasterEgg
//import com.android_j.egg.AndroidJellyBeanEasterEgg
//import com.android_k.egg.AndroidKitKatEasterEgg
//import com.android_l.egg.AndroidLollipopEasterEgg
//import com.android_m.egg.AndroidMarshmallowEasterEgg
//import com.android_n.egg.AndroidNougatEasterEgg
import com.android_next.egg.AndroidNextEasterEgg
//import com.android_o.egg.AndroidOreoEasterEgg
//import com.android_p.egg.AndroidPieEasterEgg
//import com.android_q.egg.AndroidQEasterEgg
//import com.android_r.egg.AndroidREasterEgg
//import com.android_s.egg.AndroidSEasterEgg
//import com.android_t.egg.AndroidTEasterEgg
import com.android_u.egg.AndroidUEasterEgg
import com.android_v.egg.AndroidVEasterEgg
import com.dede.android_eggs.views.timeline.TimelineEventHelp
import com.dede.basic.provider.BaseEasterEgg
import com.dede.basic.provider.ComponentProvider.Component
import com.dede.basic.provider.EasterEgg
import com.dede.basic.provider.EasterEggGroup
import com.dede.basic.provider.TimelineEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
//        AndroidNextEasterEgg::class,
        AndroidVEasterEgg::class,
//        AndroidUEasterEgg::class,
//        AndroidTEasterEgg::class,
//        AndroidSEasterEgg::class,
//        AndroidREasterEgg::class,
//        AndroidQEasterEgg::class,
//        AndroidPieEasterEgg::class,
//        AndroidOreoEasterEgg::class,
//        AndroidNougatEasterEgg::class,
//        AndroidMarshmallowEasterEgg::class,
//        AndroidLollipopEasterEgg::class,
//        AndroidKitKatEasterEgg::class,
//        AndroidJellyBeanEasterEgg::class,
//        AndroidIceCreamSandwichEasterEgg::class,
//        AndroidHoneycombEasterEgg::class,
//        AndroidGingerbreadEasterEgg::class,
        AndroidBaseEasterEgg::class,
    ],
)
@InstallIn(SingletonComponent::class)
object EasterEggModules {

    @Provides
    @Singleton
    fun provideTimelineEventList(timelineGroupSet: Set<@JvmSuppressWildcards List<@JvmSuppressWildcards TimelineEvent>>): List<@JvmSuppressWildcards TimelineEvent> {
        return buildList {
            for (events in timelineGroupSet) {
                addAll(events)
            }
        }.sortedWith(TimelineEventHelp.EventComparator)
    }

    @Provides
    @Singleton
    fun provideEasterEggList(easterEggSet: Set<@JvmSuppressWildcards BaseEasterEgg>): List<@JvmSuppressWildcards BaseEasterEgg> {
        return easterEggSet.sortedByDescending { it.apiLevel }
    }

    @Provides
    @Singleton
    fun providePureEasterEggList(easterEggs: List<@JvmSuppressWildcards BaseEasterEgg>): List<@JvmSuppressWildcards EasterEgg> {
        return buildList {
            for (easterEgg in easterEggs) {
                if (easterEgg is EasterEggGroup) {
                    addAll(easterEgg.eggs)
                } else if (easterEgg is EasterEgg) {
                    add(easterEgg)
                }
            }
        }
    }

}

@Module(
    includes = [
        AndroidVEasterEgg::class,
//        AndroidTEasterEgg::class,
//        AndroidSEasterEgg::class,
//        AndroidREasterEgg::class,
//        AndroidNougatEasterEgg::class,
//        AndroidKitKatEasterEgg::class,
//        AndroidJellyBeanEasterEgg::class,
    ],
)
@InstallIn(SingletonComponent::class)
object EasterEggComponentModules {

    @Provides
    @Singleton
    fun provideEasterEggComponents(componentSet: Set<@JvmSuppressWildcards Component>): List<@JvmSuppressWildcards Component> {
        return componentSet
            .filter { it.isSupported() }
            .sortedByDescending { it.apiLevel }
    }

}
