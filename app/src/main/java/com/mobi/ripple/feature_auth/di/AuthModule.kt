package com.mobi.ripple.feature_auth.di

import com.mobi.ripple.feature_auth.data.data_source.remote.AuthApiService
import com.mobi.ripple.feature_auth.data.data_source.remote.AuthApiServiceImpl
import com.mobi.ripple.feature_auth.data.repository.AuthRepositoryImpl
import com.mobi.ripple.feature_auth.domain.repository.AuthRepository
import com.mobi.ripple.feature_auth.domain.use_case.AuthUseCases
import com.mobi.ripple.feature_auth.domain.use_case.IsEmailTakenUseCase
import com.mobi.ripple.feature_auth.domain.use_case.IsUsernameTakenUseCase
import com.mobi.ripple.feature_auth.domain.use_case.LoginUseCase
import com.mobi.ripple.feature_auth.domain.use_case.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApiService(client: HttpClient): AuthApiService {
        return AuthApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApiService: AuthApiService): AuthRepository {
        return AuthRepositoryImpl(authApiService)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(authRepository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            loginUseCase = LoginUseCase(authRepository),
            registerUseCase = RegisterUseCase(authRepository),
            isUsernameTakenUseCase = IsUsernameTakenUseCase(authRepository),
            isEmailTakenUseCase = IsEmailTakenUseCase(authRepository)
        )
    }
}