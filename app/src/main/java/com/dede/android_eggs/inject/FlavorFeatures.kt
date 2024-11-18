package com.dede.android_eggs.inject

import androidx.fragment.app.FragmentActivity
import com.dede.android_eggs.FlavorFeaturesImpl

interface FlavorFeatures {

    companion object {

        const val TAG = "FlavorFeatures"

        fun get(): FlavorFeatures {
            return FlavorFeaturesImpl()
        }
    }

    fun call(activity: FragmentActivity)
}
