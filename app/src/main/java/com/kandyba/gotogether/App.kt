package com.kandyba.gotogether

import android.app.Application
import com.kandyba.gotogether.di.DaggerAppComponent

class App: Application() {
    val appComponent = DaggerAppComponent
        .factory()
        .create(this)
}