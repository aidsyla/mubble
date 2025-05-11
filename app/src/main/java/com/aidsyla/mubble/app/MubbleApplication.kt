package com.aidsyla.mubble.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MubbleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}