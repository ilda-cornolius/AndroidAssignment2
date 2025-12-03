package com.example.Lab2_Start

import android.app.Application
import com.example.Lab2_Start.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application class for initializing Koin dependency injection.
 */
class TaskApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TaskApp)
            modules(appModule)
        }
    }
}


