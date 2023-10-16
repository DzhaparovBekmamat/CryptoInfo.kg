package com.template.flowwithretrofit.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.template.flowwithretrofit.api.ApiServices
import com.template.flowwithretrofit.utils.Constants.BASE_URL
import com.template.flowwithretrofit.utils.Constants.NETWORK_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// Этот модуль предоставляет зависимости для работы с API, такие как Retrofit, Gson и OkHttpClient.
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    // Предоставляет базовый URL для API.
    @Provides
    fun provideBaseUrl() = BASE_URL

    // Предоставляет таймаут для сетевых операций.
    @Provides
    fun provideNetworkTome() = NETWORK_TIMEOUT

    // Предоставляет экземпляр Gson для работы с JSON данными.
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    // Предоставляет перехватчик для логирования запросов и ответов.
    @Provides
    @Singleton
    fun provideBodyInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Предоставляет клиент OkHttpClient с настройками, такими как таймауты и перехватчик.
    @Provides
    @Singleton
    fun provideClient(
        time: Long, body: HttpLoggingInterceptor,
    ) = OkHttpClient.Builder().addInterceptor(body).connectTimeout(time, TimeUnit.SECONDS)
        .readTimeout(time, TimeUnit.SECONDS).writeTimeout(time, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true).build()

    // Предоставляет экземпляр Retrofit для взаимодействия с API.
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient, gson: Gson): ApiServices =
        Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiServices::class.java)
}
