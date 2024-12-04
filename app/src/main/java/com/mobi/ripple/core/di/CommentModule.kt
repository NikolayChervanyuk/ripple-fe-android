package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.comment.data_source.remote.CommentApiService
import com.mobi.ripple.core.data.comment.data_source.remote.CommentApiServiceImpl
import com.mobi.ripple.core.data.comment.repository.CommentRepositoryImpl
import com.mobi.ripple.core.domain.post.repository.CommentRepository
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