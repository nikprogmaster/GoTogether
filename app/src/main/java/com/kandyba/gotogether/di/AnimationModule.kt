package com.kandyba.gotogether.di

import com.kandyba.gotogether.presentation.animation.StartAppAnimation
import dagger.Module
import dagger.Provides

@Module
class AnimationModule {

    @Provides
    fun provideStartAppAnimation(): StartAppAnimation {
        return StartAppAnimation()
    }
}