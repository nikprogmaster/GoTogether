package com.kandyba.gotogether.di

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kandyba.gotogether.data.api.AuthApiMapper
import com.kandyba.gotogether.data.api.EventsApiMapper
import com.kandyba.gotogether.data.api.MessagesApiMapper
import com.kandyba.gotogether.data.api.UserApiMapper
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
class NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(true)
            .callTimeout(60, TimeUnit.SECONDS)
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

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideAuthApiMapper(retrofit: Retrofit): AuthApiMapper {
        return retrofit.create(AuthApiMapper::class.java)
    }

    @Provides
    fun provideEventsMapper(retrofit: Retrofit): EventsApiMapper {
        return retrofit.create(EventsApiMapper::class.java)
    }

    @Provides
    fun provideUserMapper(retrofit: Retrofit): UserApiMapper {
        return retrofit.create(UserApiMapper::class.java)
    }

    @Provides
    fun provideMessagesMapper(retrofit: Retrofit): MessagesApiMapper {
        return retrofit.create(MessagesApiMapper::class.java)
    }

    companion object {
        private const val BASE_URL = "http://51.15.104.77:8085/api/"
    }
}