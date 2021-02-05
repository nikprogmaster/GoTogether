package com.kandyba.gotogether.di

import android.content.Context
import android.content.SharedPreferences
import com.kandyba.gotogether.presentation.animation.StartAppAnimation
import com.kandyba.gotogether.presentation.viewmodel.factory.StartViewModelFactory
import dagger.BindsInstance
import dagger.Component

@Component(modules =
[
    NetworkModule::class,
    ViewModelModule::class,
    SharedPreferencesModule::class,
    AnimationModule::class
])
interface AppComponent {

    fun getStartViewModelFactory(): StartViewModelFactory

    fun getSharedPreferences(): SharedPreferences

    fun getStartAnimation(): StartAppAnimation

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}