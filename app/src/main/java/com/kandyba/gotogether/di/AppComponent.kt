package com.kandyba.gotogether.di

import android.content.Context
import android.content.SharedPreferences
import com.kandyba.gotogether.presentation.animation.StartAppAnimation
import com.kandyba.gotogether.presentation.viewmodel.factory.EventDetailsViewModelFactory
import com.kandyba.gotogether.presentation.viewmodel.factory.ForYouViewModelFactory
import com.kandyba.gotogether.presentation.viewmodel.factory.SearchViewModelFactory
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

    fun getForYouViewModelFactory(): ForYouViewModelFactory

    fun getEventDetailsViewModelFactory(): EventDetailsViewModelFactory

    fun getSearchViewModelFactory(): SearchViewModelFactory

    fun getSharedPreferences(): SharedPreferences

    fun getStartAnimation(): StartAppAnimation

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}