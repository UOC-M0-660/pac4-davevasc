@file:Suppress("unused")

package edu.uoc.pac4

import android.app.Application
import edu.uoc.pac4.data.di.dataModule
import edu.uoc.pac4.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
* Created by david on 01/01/21.
* Application class for init Koin
*/

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin {
            // Declare Logger
            androidLogger()
            // Declare used Android context
            androidContext(this@App)
            // Declare modules
            modules(arrayListOf(dataModule, uiModule))
        }
    }

}