package com.mobi.ripple.feature_app.feature_chat.di

import android.content.Context
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.feature_app.MessageManager
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.ChatApiService
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.ChatApiServiceImpl
import com.mobi.ripple.feature_app.feature_chat.data.repository.ChatRepositoryImpl
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.ChatUseCases
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.CreateNewChatUseCase
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.FindChatUsersLikeUseCase
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.GetChatParticipantsUseCase
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.GetChatUseCase
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.GetChatUserUseCase
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.GetChatsFlowUseCase
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.GetMessagesFlowUseCase
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.HasPendingMessagesUseCase
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import com.mobi.ripple.feature_app.feature_chat.service.util.NotificationHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatApiService(client: HttpClient): ChatApiService {
        return ChatApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        apiService: ChatApiService,
        database: AppDatabase
    ): ChatRepository {
        return ChatRepositoryImpl(apiService, database)
    }

    @Provides
    @Singleton
    fun provideChatUseCases(repository: ChatRepository): ChatUseCases {
        return ChatUseCases(
            getChatsFlowUseCase = GetChatsFlowUseCase(repository),
            findChatUsersLikeUseCase = FindChatUsersLikeUseCase(repository),
            createNewChatUseCase = CreateNewChatUseCase(repository),
            getChatUserUseCase = GetChatUserUseCase(repository),
            hasPendingMessagesUseCase = HasPendingMessagesUseCase(repository),
            getChatParticipantsUseCase = GetChatParticipantsUseCase(repository),
            getChatUseCase = GetChatUseCase(repository),
            getMessagesFlowUseCase = GetMessagesFlowUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideMessageCacheManager(
        database: AppDatabase,
        chatUseCases: ChatUseCases
    ): MessageCacheManager {
        return MessageCacheManager(database, chatUseCases)
    }

    @Provides
    @Singleton
    fun provideMessageManager(
        @ApplicationContext context: Context,
        cacheManager: MessageCacheManager,
        client: HttpClient
    ): MessageManager {
        return MessageManager(
            context = context,
            cacheManager = cacheManager,
            client = client
        )
    }

    @Provides
    @Singleton
    fun provideNotificationHandler(
        @ApplicationContext context: Context,
        database: AppDatabase
    ): NotificationHandler {
        return NotificationHandler(context, database)
    }
}