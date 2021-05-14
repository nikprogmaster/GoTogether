package com.kandyba.gotogether.di

import android.content.Context
import android.content.SharedPreferences
import com.kandyba.gotogether.presentation.animation.StartAppAnimation
import com.kandyba.gotogether.presentation.viewmodel.factory.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Dagger-компонент, собирающий в себе модули с зависимостями
 */
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

    /**
     * Предоставить фабрику для получения StartViewModel
     *
     * @return [SearchViewModelFactory]
     */
    fun getStartViewModelFactory(): StartViewModelFactory

    /**
     * Предоставить фабрику для получения ForYouViewModel
     *
     * @return [ForYouViewModelFactory]
     */
    fun getForYouViewModelFactory(): ForYouViewModelFactory

    /**
     * Предоставить фабрику для получения EventDetailsViewModel
     *
     * @return [EventDetailsViewModelFactory]
     */
    fun getEventDetailsViewModelFactory(): EventDetailsViewModelFactory

    /**
     * Предоставить фабрику для получения SearchViewModel
     *
     * @return [SearchViewModelFactory]
     */
    fun getSearchViewModelFactory(): SearchViewModelFactory

    /**
     * Предоставить фабрику для получения MainViewModel
     *
     * @return [MainViewModelFactory]
     */
    fun getMainViewModelFactory(): MainViewModelFactory

    /**
     * Предоставить фабрику для получения ProfileViewModel
     *
     * @return [ProfileViewModelFactory]
     */
    fun getProfileViewModelFactory(): ProfileViewModelFactory

    /**
     * Предоставить фабрику для получения DialogsViewModel
     *
     * @return [DialogsViewModelFactory]
     */
    fun getDialogsViewModelFactory(): DialogsViewModelFactory

    /**
     * Предоставить настройки приложения
     *
     * @return [SharedPreferences]
     */
    fun getSharedPreferences(): SharedPreferences

    /**
     * Предоставить стартовую анимацию
     *
     * @return [StartAppAnimation]
     */
    fun getStartAnimation(): StartAppAnimation

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
