package com.branch.core_database.di

import android.content.Context
import com.branch.core_database.data.AppDataStoreImpl
import com.branch.core_database.domain.AppDataStore
import com.branch.core_utils.utils.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context,
        appDispatchers: AppDispatchers
    ): AppDataStore = AppDataStoreImpl(context, appDispatchers)

}