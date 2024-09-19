package com.mobi.ripple.core.di

import com.mobi.ripple.core.data.data_source.remote.reply.ReplyApiService
import com.mobi.ripple.core.data.data_source.remote.reply.ReplyApiServiceImpl
import com.mobi.ripple.core.data.repository.ReplyRepositoryImpl
import com.mobi.ripple.core.domain.repository.ReplyRepository
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
    fun provideReplyRepository(replyApiService: ReplyApiService): ReplyRepository {
      return ReplyRepositoryImpl(replyApiService)
    }
}