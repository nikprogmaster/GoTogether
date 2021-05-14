package com.kandyba.gotogether.di

import android.content.Context
import android.content.SharedPreferences
import com.kandyba.gotogether.models.general.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides

/**
 * Dagger-модуль, предоставляющий инстанс [SharedPreferences]
 */
@Module
class SharedPreferencesModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}