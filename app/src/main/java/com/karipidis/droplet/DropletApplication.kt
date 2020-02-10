package com.karipidis.droplet

import android.app.Application
import com.karipidis.droplet.di.detailsModule
import com.karipidis.droplet.di.userRepositoryModule
import com.karipidis.droplet.di.welcomeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DropletApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DropletApplication)
            modules(welcomeModule, detailsModule, userRepositoryModule)
        }
    }
}