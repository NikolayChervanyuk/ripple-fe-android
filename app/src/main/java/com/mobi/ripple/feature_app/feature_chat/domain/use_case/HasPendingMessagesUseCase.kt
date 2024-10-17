package com.mobi.ripple.feature_app.feature_chat.domain.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository

class HasPendingMessagesUseCase(
    val repository: ChatRepository
) {
    suspend operator fun invoke(): Response<Boolean?> {
        return repository.hasPendingMessages()
    }
}