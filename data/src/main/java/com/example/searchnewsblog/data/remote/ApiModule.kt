package com.example.searchnewsblog.data.remote

import com.example.searchnewsblog.data.BuildConfig
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor? = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    } else {
        null
    }

    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(
        interceptor: Interceptor?,
    ) = OkHttpClient.Builder().apply {
        interceptor?.let { addInterceptor(it) }
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
    ): Retrofit = Retrofit
        .Builder()
        .client(client)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create()
            )
        )
        .baseUrl(BuildConfig.BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit,
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}