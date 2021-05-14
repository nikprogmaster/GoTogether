package com.kandyba.gotogether.di

import com.kandyba.gotogether.presentation.animation.StartAppAnimation
import dagger.Module
import dagger.Provides

/**
 * Dagger-модуль, предоставляющий инстанс анимации
 */
@Module
class AnimationModule {

    @Provides
    fun provideStartAppAnimation(): StartAppAnimation {
        return StartAppAnimation()
    }
}

