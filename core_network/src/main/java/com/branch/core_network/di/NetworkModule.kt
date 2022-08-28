package com.branch.core_network.di

import com.branch.core_database.domain.AppDataStore
import com.branch.core_network.BuildConfig
import com.branch.core_network.data.TokenInterceptor
import com.branch.core_utils.utils.AppDispatchers
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKotlinJson() = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(
        appDataStore: AppDataStore, appDispatchers: AppDispatchers
    ): TokenInterceptor = TokenInterceptor(appDispatchers, appDataStore)

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenInterceptor: TokenInterceptor) = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        addInterceptor(tokenInterceptor)
    }.build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(json: Json, okHttpClient: OkHttpClient) =
        Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).baseUrl(BuildConfig.BASE_URL)
            .build()

}