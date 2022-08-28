package com.branch.feature_auth.di

import com.branch.core_database.domain.AppDataStore
import com.branch.feature_auth.data.api.LoginService
import com.branch.feature_auth.data.repository.LoginRepoImple
import com.branch.feature_auth.domain.repository.LoginRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn( SingletonComponent::class )
object AuthModules {

    @Provides
    @Singleton
    fun providesLoginService( retrofit: Retrofit ) = retrofit.create( LoginService::class.java )

    @Provides
    fun provideLoginRepo(  loginService: LoginService, appDataStore: AppDataStore) : LoginRepo = LoginRepoImple(
        loginService, appDataStore
    )
}