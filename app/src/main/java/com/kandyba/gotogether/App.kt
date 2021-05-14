package com.kandyba.gotogether

import android.app.Application
import com.kandyba.gotogether.di.DaggerAppComponent
import com.kandyba.gotogether.models.general.MAP_KIT_KEY
import com.yandex.mapkit.MapKitFactory

/**
 * Класс, инициализирующий карту и dagger-компонент приложения
 */
class App: Application() {

    /**
     * Dagger-компонент приложения
     */
    val appComponent = DaggerAppComponent
        .factory()
        .create(this)

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAP_KIT_KEY)
    }
}
