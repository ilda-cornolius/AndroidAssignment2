package com.example.Lab2_Start

import android.app.Application
import com.example.Lab2_Start.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


 //Initialized when the application starts 
 //The onCreate TaskApp method runs
 //It initializes Koin and registers all dependencies from appModule
 //Koin can create objects on demand and can inject dependencies when needed automatically 
class TaskApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TaskApp)
            modules(appModule)
        }
    }
}


