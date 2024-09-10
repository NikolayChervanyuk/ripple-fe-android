package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.data_source.local.AppDatabase
import com.mobi.ripple.core.data.data_source.remote.post.PostApiService
import com.mobi.ripple.core.data.data_source.remote.post.PostApiServiceImpl
import com.mobi.ripple.core.data.repository.PostRepositoryImpl
import com.mobi.ripple.core.domain.repository.PostRepository
import com.mobi.ripple.core.domain.use_case.post.ChangePostLikeStateUseCase
import com.mobi.ripple.core.domain.use_case.post.GetPostCommentsFlowUseCase
import com.mobi.ripple.core.domain.use_case.post.GetPostUseCase
import com.mobi.ripple.core.domain.use_case.post.GetSimpleUserUseCase
import com.mobi.ripple.core.domain.use_case.post.PostUseCases
import com.mobi.ripple.core.domain.use_case.post.UploadCommentUseCase
import com.mobi.ripple.core.domain.use_case.posts.GetPostsUseCase
import com.mobi.ripple.core.domain.use_case.posts.PostsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {

    @Provides
    @Singleton
    fun providePostApiService(client: HttpClient): PostApiService {
        return PostApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun providePostRepositoryImpl(
        apiService: PostApiService,
        database: AppDatabase
    ): PostRepository {
        return PostRepositoryImpl(apiService, database)
    }

    @Provides
    @Singleton
    fun providePostUseCases(repository: PostRepository): PostUseCases {
        return PostUseCases(
            getPostUseCase = GetPostUseCase(repository),
            getSimpleUserUseCase = GetSimpleUserUseCase(repository),
            changePostLikeStateUseCase = ChangePostLikeStateUseCase(repository),
            getPostCommentsFlowUseCase = GetPostCommentsFlowUseCase(repository),
            uploadCommentUseCase = UploadCommentUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun providePostsUseCases(repository: PostRepository): PostsUseCases {
      return PostsUseCases(
          getPostsUseCase = GetPostsUseCase(repository)
      )
    }
}