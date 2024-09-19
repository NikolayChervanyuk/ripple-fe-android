package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.data_source.remote.comment.CommentApiService
import com.mobi.ripple.core.data.data_source.remote.comment.CommentApiServiceImpl
import com.mobi.ripple.core.data.repository.CommentRepositoryImpl
import com.mobi.ripple.core.domain.repository.CommentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommentModule {

    @Provides
    @Singleton
    fun provideCommentApiService(client: HttpClient): CommentApiService {
        return CommentApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideCommentRepository(apiService: CommentApiService): CommentRepository {
        return CommentRepositoryImpl(apiService)
    }
}