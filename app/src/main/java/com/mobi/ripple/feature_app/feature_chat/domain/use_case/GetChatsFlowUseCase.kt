package com.mobi.ripple.feature_app.feature_chat.domain.use_case

import androidx.paging.PagingData
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChat
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import kotlinx.coroutines.flow.Flow

class GetChatsFlowUseCase(
    val repository: ChatRepository
) {
    suspend operator fun invoke(cacheManager: MessageCacheManager): Flow<PagingData<SimpleChat>> {
        return repository.getChatsFlow(cacheManager)
    }
}