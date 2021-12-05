package com.hyundai.placefinder

import android.app.Application
import com.hyundai.placefinder.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PlaceFinderApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlaceFinderApplication)
            androidLogger(Level.DEBUG)
            loadKoinModules(listOf(
                appModule,
                localModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            ))
        }
    }

}