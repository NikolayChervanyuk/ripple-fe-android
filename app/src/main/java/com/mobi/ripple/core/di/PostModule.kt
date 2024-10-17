package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.post.data_source.remote.PostApiService
import com.mobi.ripple.core.data.post.data_source.remote.PostApiServiceImpl
import com.mobi.ripple.core.data.post.repository.PostRepositoryImpl
import com.mobi.ripple.core.domain.post.repository.CommentRepository
import com.mobi.ripple.core.domain.post.repository.PostRepository
import com.mobi.ripple.core.domain.post.repository.ReplyRepository
import com.mobi.ripple.core.domain.post.use_case.ChangePostLikeStateUseCase
import com.mobi.ripple.core.domain.post.use_case.GetPostCommentsFlowUseCase
import com.mobi.ripple.core.domain.post.use_case.GetPostUseCase
import com.mobi.ripple.core.domain.post.use_case.GetSimpleUserUseCase
import com.mobi.ripple.core.domain.post.use_case.PostUseCases
import com.mobi.ripple.core.domain.post.use_case.comment.ChangeCommentLikeStateUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.DeleteCommentUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.EditCommentUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.UploadCommentUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.ChangeReplyLikeStateUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.DeleteReplyUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.EditReplyUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.GetCommentRepliesFlowUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.UploadReplyUseCase
import com.mobi.ripple.core.domain.post.use_case.posts.GetPostsUseCase
import com.mobi.ripple.core.domain.post.use_case.posts.PostsUseCases
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
    fun providePostUseCases(
        postRepository: PostRepository,
        commentRepository: CommentRepository,
        replyRepository: ReplyRepository
    ): PostUseCases {
        return PostUseCases(
            getPostUseCase = GetPostUseCase(postRepository),
            getSimpleUserUseCase = GetSimpleUserUseCase(postRepository),
            changePostLikeStateUseCase = ChangePostLikeStateUseCase(postRepository),
            getPostCommentsFlowUseCase = GetPostCommentsFlowUseCase(postRepository),

            uploadCommentUseCase = UploadCommentUseCase(commentRepository),
            changeCommentLikeStateUseCase = ChangeCommentLikeStateUseCase(commentRepository),
            editCommentUseCase = EditCommentUseCase(commentRepository),
            deleteCommentUseCase = DeleteCommentUseCase(commentRepository),
            getCommentRepliesFlowUseCase = GetCommentRepliesFlowUseCase(replyRepository),

            uploadReplyUseCase = UploadReplyUseCase(replyRepository),
            changeReplyLikeStateUseCase = ChangeReplyLikeStateUseCase(replyRepository),
            editReplyUseCase = EditReplyUseCase(replyRepository),
            deleteReplyUseCase = DeleteReplyUseCase(replyRepository)
        )
    }

    @Provides
    @Singleton
    fun providePostsUseCases(repository: PostRepository): PostsUseCases {
        return PostsUseCases(
            getPostsUseCase = GetPostsUseCase(repository),
//            getFirstCachedPostUseCase = GetFirstCachedPostUseCase(repository)
        )
    }
}