package com.kandyba.gotogether

import android.app.Application
import com.kandyba.gotogether.di.DaggerAppComponent
import com.kandyba.gotogether.models.general.MAP_KIT_KEY
import com.yandex.mapkit.MapKitFactory

class App: Application() {
    val appComponent = DaggerAppComponent
        .factory()
        .create(this)

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAP_KIT_KEY)
    }
}
