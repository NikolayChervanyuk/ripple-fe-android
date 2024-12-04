package com.mobi.ripple.feature_app.feature_search.di

import com.mobi.ripple.feature_app.feature_search.data.data_source.remote.SearchApiService
import com.mobi.ripple.feature_app.feature_search.data.data_source.remote.SearchApiServiceImpl
import com.mobi.ripple.feature_app.feature_search.data.repository.SearchRepositoryImpl
import com.mobi.ripple.feature_app.feature_search.domain.repository.SearchRepository
import com.mobi.ripple.feature_app.feature_search.domain.use_case.FindUsersLikeUsernameUseCase
import com.mobi.ripple.feature_app.feature_search.domain.use_case.SearchUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideSearchApiService(client: HttpClient): SearchApiService {
      return SearchApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(searchApiService: SearchApiService): SearchRepository {
        return SearchRepositoryImpl(searchApiService)
    }

    @Provides
    @Singleton
    fun provideSearchUseCases(repository: SearchRepository): SearchUseCases {
        return SearchUseCases(
            findUsersLikeUsernameUseCase = FindUsersLikeUsernameUseCase(repository)
        )

    }
}