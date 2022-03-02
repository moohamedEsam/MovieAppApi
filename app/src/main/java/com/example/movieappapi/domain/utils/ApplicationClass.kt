package com.example.movieappapi.domain.utils

import android.app.Application
import android.util.Log
import com.example.movieappapi.presentation.koin.repositoryModule
import com.example.movieappapi.presentation.koin.useCaseModule
import com.example.movieappapi.presentation.koin.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        val logger = object : Logger() {
            override fun log(level: Level, msg: MESSAGE) {
                Log.i("koin", "log: $msg")
            }
        }
        startKoin {
            allowOverride(true)
            androidContext(this@ApplicationClass)
            modules(viewModelsModule, repositoryModule, useCaseModule)
            logger(logger)
        }
    }
}