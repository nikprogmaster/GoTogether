package com.kandyba.gotogether.di

import com.kandyba.gotogether.data.api.AuthApiMapper
import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.api.MessagesApiMapper
import com.kandyba.gotogether.data.api.UserApiMapper
import com.kandyba.gotogether.data.converter.auth.LoginDataResponseConverter
import com.kandyba.gotogether.data.converter.auth.SignupDataResponseConverter
import com.kandyba.gotogether.data.converter.events.EventDetailsDataConverter
import com.kandyba.gotogether.data.converter.events.EventsDataConverter
import com.kandyba.gotogether.data.converter.messages.DialogDataConverter
import com.kandyba.gotogether.data.converter.messages.MessageDataConverter
import com.kandyba.gotogether.data.converter.users.ParticipantsConverter
import com.kandyba.gotogether.data.converter.users.UserDataConverter
import com.kandyba.gotogether.data.repository.AuthRepositoryImpl
import com.kandyba.gotogether.data.repository.EventsRepositoryImpl
import com.kandyba.gotogether.data.repository.MessagesRepositoryImpl
import com.kandyba.gotogether.data.repository.UserRepositoryImpl
import com.kandyba.gotogether.domain.auth.AuthInteractor
import com.kandyba.gotogether.domain.auth.AuthInteractorImpl
import com.kandyba.gotogether.domain.events.EventsDomainConverter
import com.kandyba.gotogether.domain.events.EventsInteractor
import com.kandyba.gotogether.domain.events.EventsInteractorImpl
import com.kandyba.gotogether.domain.message.MessageDomainConverter
import com.kandyba.gotogether.domain.message.MessagesInteractor
import com.kandyba.gotogether.domain.message.MessagesInteractorImpl
import com.kandyba.gotogether.domain.user.UserDomainConverter
import com.kandyba.gotogether.domain.user.UserInteractor
import com.kandyba.gotogether.domain.user.UserInteractorImpl
import com.kandyba.gotogether.models.general.NetworkError
import com.kandyba.gotogether.presentation.viewmodel.*
import com.kandyba.gotogether.presentation.viewmodel.factory.*
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ViewModelModule {

    @Provides
    fun provideStartViewModelFactory(
        userInteractor: UserInteractor,
        eventsInteractor: EventsInteractor,
        authInteractor: AuthInteractor
    ): StartViewModelFactory {
        val gsonConverter = GsonConverterFactory.create().responseBodyConverter(
            NetworkError::class.java, null, null
        )

        return StartViewModelFactory {
            StartViewModel(authInteractor, eventsInteractor, userInteractor, gsonConverter)
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
        eventsInteractor: EventsInteractor,
        userInteractor: UserInteractor
    ): EventDetailsViewModelFactory {
        return EventDetailsViewModelFactory {
            EventDetailsViewModel(eventsInteractor, userInteractor)
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
    fun provideMainViewModelFactory(): MainViewModelFactory {
        return MainViewModelFactory {
            MainViewModel()
        }
    }

    @Provides
    fun provideProfileViewModelFactory(
        userInteractor: UserInteractor,
        authInteractor: AuthInteractor,
        eventsInteractor: EventsInteractor
    ): ProfileViewModelFactory {
        return ProfileViewModelFactory {
            ProfileViewModel(userInteractor, authInteractor, eventsInteractor)
        }
    }

    @Provides
    fun provideDialogsViewModelFactory(
        messagesInteractor: MessagesInteractor
    ): DialogsViewModelFactory {
        return DialogsViewModelFactory {
            DialogsViewModel(messagesInteractor)
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
    fun provideUserInteractor(userMapper: UserApiMapper): UserInteractor {
        return UserInteractorImpl(
            UserRepositoryImpl(
                userMapper,
                UserDataConverter(EventDetailsDataConverter()),
                ParticipantsConverter()
            ),
            UserDomainConverter(EventsDomainConverter())
        )
    }

    @Provides
    fun provideEventsInteractor(eventsMapper: EventsApiMapper): EventsInteractor {
        val eventDetailsDataConverter = EventDetailsDataConverter()
        return EventsInteractorImpl(
            EventsRepositoryImpl(
                eventsMapper,
                EventsDataConverter(eventDetailsDataConverter),
                eventDetailsDataConverter
            ),
            EventsDomainConverter()
        )
    }

    @Provides
    fun provideMessageInteractor(messagesApiMapper: MessagesApiMapper): MessagesInteractor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("Connection", "close")
                        .build()
                    return chain.proceed(request)
                }
            })
            .build()
        return MessagesInteractorImpl(
            MessagesRepositoryImpl(
                messagesApiMapper,
                MessageDataConverter(),
                DialogDataConverter(UserDataConverter(EventDetailsDataConverter()))
            ),
            MessageDomainConverter()
        )
    }
}