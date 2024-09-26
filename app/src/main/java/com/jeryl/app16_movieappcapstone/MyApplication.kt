package com.jeryl.app16_movieappcapstone

import android.app.Application
import com.jeryl.app16_movieappcapstone.core.di.databaseModule
import com.jeryl.app16_movieappcapstone.core.di.networkModule
import com.jeryl.app16_movieappcapstone.core.di.repositoryModule
import com.jeryl.app16_movieappcapstone.di.useCaseModule
import com.jeryl.app16_movieappcapstone.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


/**
 * Created by Jery I D Lenas on 18/09/2024.
 * Contact : jerylenas@gmail.com
 */

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                )
            )
        }
    }
}