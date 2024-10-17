package com.mobi.ripple.feature_app.feature_chat.domain.use_case

import androidx.paging.PagingData
import com.mobi.ripple.feature_app.feature_chat.domain.model.Message
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import kotlinx.coroutines.flow.Flow


class GetMessagesFlowUseCase(
    val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, cacheManager: MessageCacheManager): Flow<PagingData<Message>> {
        return repository.getMessagesFlow(chatId, cacheManager)
    }
}