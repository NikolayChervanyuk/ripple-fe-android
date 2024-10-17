package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.reply.data_source.remote.ReplyApiService
import com.mobi.ripple.core.data.reply.data_source.remote.ReplyApiServiceImpl
import com.mobi.ripple.core.data.reply.repository.ReplyRepositoryImpl
import com.mobi.ripple.core.domain.post.repository.ReplyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReplyModule {

    @Provides
    @Singleton
    fun provideReplyApiService(client: HttpClient): ReplyApiService {
      return ReplyApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideReplyRepository(replyApiService: ReplyApiService, database: AppDatabase): ReplyRepository {
      return ReplyRepositoryImpl(replyApiService, database)
    }
}