package com.example.homehub

import android.app.Application
import com.example.homehub.utils.StartupConfigurator
import com.example.homehub.utils.Logs
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var startupConfigurator: StartupConfigurator

    override fun onCreate() {
        super.onCreate()
        Logs.initialize(this)
        startupConfigurator.initialize()
    }
}