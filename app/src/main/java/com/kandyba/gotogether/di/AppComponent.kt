package com.kandyba.gotogether.di

import android.content.Context
import android.content.SharedPreferences
import com.kandyba.gotogether.presentation.animation.StartAppAnimation
import com.kandyba.gotogether.presentation.viewmodel.factory.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules =
    [
        NetworkModule::class,
        ViewModelModule::class,
        SharedPreferencesModule::class,
        AnimationModule::class
    ]
)
@Singleton
interface AppComponent {

    fun getStartViewModelFactory(): StartViewModelFactory

    fun getForYouViewModelFactory(): ForYouViewModelFactory

    fun getEventDetailsViewModelFactory(): EventDetailsViewModelFactory

    fun getSearchViewModelFactory(): SearchViewModelFactory

    fun getMainViewModelFactory(): MainViewModelFactory

    fun getProfileViewModelFactory(): ProfileViewModelFactory

    fun getDialogsViewModelFactory(): DialogsViewModelFactory

    fun getSharedPreferences(): SharedPreferences

    fun getStartAnimation(): StartAppAnimation

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
