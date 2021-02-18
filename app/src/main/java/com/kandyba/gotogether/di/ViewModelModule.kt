package com.kandyba.gotogether.di

import com.kandyba.gotogether.data.api.AuthApiMapper
import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.api.UserApiMapper
import com.kandyba.gotogether.data.converter.auth.EventsDataConverter
import com.kandyba.gotogether.data.converter.auth.LoginDataResponseConverter
import com.kandyba.gotogether.data.converter.auth.SignupDataResponseConverter
import com.kandyba.gotogether.data.converter.auth.UserDataConverter
import com.kandyba.gotogether.data.repository.AuthRepositoryImpl
import com.kandyba.gotogether.data.repository.EventsRepositoryImpl
import com.kandyba.gotogether.data.repository.UserRepositoryImpl
import com.kandyba.gotogether.domain.auth.AuthInteractorImpl
import com.kandyba.gotogether.domain.auth.LoginDomainResponseConverter
import com.kandyba.gotogether.domain.auth.SignupDomainResponseConverter
import com.kandyba.gotogether.domain.events.EventsDomainConverter
import com.kandyba.gotogether.domain.events.EventsInteractorImpl
import com.kandyba.gotogether.domain.user.UserDomainConverter
import com.kandyba.gotogether.domain.user.UserInteractorImpl
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel
import com.kandyba.gotogether.presentation.viewmodel.factory.StartViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun provideStartViewModelFactory(
        authMapper: AuthApiMapper,
        eventsMapper: EventsApiMapper,
        userMapper: UserApiMapper
    ): StartViewModelFactory {
        val authInteractor = AuthInteractorImpl(
            AuthRepositoryImpl(
                authMapper,
                LoginDataResponseConverter(),
                SignupDataResponseConverter()
            ),
            LoginDomainResponseConverter(),
            SignupDomainResponseConverter()
        )
        val eventsInteractor = EventsInteractorImpl(
            EventsRepositoryImpl(eventsMapper, EventsDataConverter()),
            EventsDomainConverter()
        )
        val userInteractor = UserInteractorImpl(
            UserRepositoryImpl(userMapper, UserDataConverter()),
            UserDomainConverter()
        )
        return StartViewModelFactory {
            StartViewModel(
                authInteractor,
                eventsInteractor,
                userInteractor
            )
        }
    }
}