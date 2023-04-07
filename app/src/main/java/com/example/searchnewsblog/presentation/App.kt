package com.example.searchnewsblog.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * lsh 2023.04.03
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}