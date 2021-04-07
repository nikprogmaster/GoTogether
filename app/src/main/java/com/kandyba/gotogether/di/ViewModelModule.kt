package com.kandyba.gotogether.di

import com.kandyba.gotogether.data.api.AuthApiMapper
import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.api.UserApiMapper
import com.kandyba.gotogether.data.converter.auth.LoginDataResponseConverter
import com.kandyba.gotogether.data.converter.auth.SignupDataResponseConverter
import com.kandyba.gotogether.data.converter.events.EventDetailsDataConverter
import com.kandyba.gotogether.data.converter.events.EventsDataConverter
import com.kandyba.gotogether.data.converter.users.UserDataConverter
import com.kandyba.gotogether.data.repository.AuthRepositoryImpl
import com.kandyba.gotogether.data.repository.EventsRepositoryImpl
import com.kandyba.gotogether.data.repository.UserRepositoryImpl
import com.kandyba.gotogether.domain.auth.AuthInteractor
import com.kandyba.gotogether.domain.auth.AuthInteractorImpl
import com.kandyba.gotogether.domain.events.EventsDomainConverter
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.domain.events.EventsInteractorImpl
import com.kandyba.gotogether.domain.user.UserDomainConverter
import com.kandyba.gotogether.domain.user.UserInteractorImpl
import com.kandyba.gotogether.models.general.ServerExceptionEntity
import com.kandyba.gotogether.presentation.viewmodel.EventDetailsViewModel
import com.kandyba.gotogether.presentation.viewmodel.ForYouViewModel
import com.kandyba.gotogether.presentation.viewmodel.SearchViewModel
import com.kandyba.gotogether.presentation.viewmodel.StartViewModel
import com.kandyba.gotogether.presentation.viewmodel.factory.EventDetailsViewModelFactory
import com.kandyba.gotogether.presentation.viewmodel.factory.ForYouViewModelFactory
import com.kandyba.gotogether.presentation.viewmodel.factory.SearchViewModelFactory
import com.kandyba.gotogether.presentation.viewmodel.factory.StartViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ViewModelModule {

    @Provides
    fun provideStartViewModelFactory(
        userMapper: UserApiMapper,
        eventsInteractor: EventsInteractor,
        authInteractor: AuthInteractor
    ): StartViewModelFactory {
        val userInteractor = UserInteractorImpl(
            UserRepositoryImpl(userMapper, UserDataConverter()),
            UserDomainConverter()
        )
        val gsonConverter = GsonConverterFactory.create().responseBodyConverter(
            ServerExceptionEntity::class.java, null, null
        )

        return StartViewModelFactory {
            StartViewModel(
                authInteractor,
                eventsInteractor,
                userInteractor,
                gsonConverter
            )
        }
    }

    @Provides
    fun provideForYouViewModelFactory(
        authInteractor: AuthInteractor,
        eventsInteractor: EventsInteractor
    ): ForYouViewModelFactory {
        return ForYouViewModelFactory {
            ForYouViewModel(authInteractor, eventsInteractor)
        }
    }

    @Provides
    fun provideEventDetailsViewModelFactory(
        eventsInteractor: EventsInteractor
    ): EventDetailsViewModelFactory {
        return EventDetailsViewModelFactory {
            EventDetailsViewModel(eventsInteractor)
        }
    }

    @Provides
    fun provideSearchViewModelFactory(
        eventsInteractor: EventsInteractor
    ): SearchViewModelFactory {
        return SearchViewModelFactory {
            SearchViewModel(eventsInteractor)
        }
    }

    @Provides
    fun provideAuthInteractor(authMapper: AuthApiMapper): AuthInteractor {
        return AuthInteractorImpl(
            AuthRepositoryImpl(
                authMapper,
                LoginDataResponseConverter(),
                SignupDataResponseConverter()
            )
        )
    }

    @Provides
    fun provideEventsInteractor(eventsMapper: EventsApiMapper): EventsInteractor {
        return EventsInteractorImpl(
            EventsRepositoryImpl(
                eventsMapper,
                EventsDataConverter(),
                EventDetailsDataConverter()
            ),
            EventsDomainConverter()
        )
    }
}