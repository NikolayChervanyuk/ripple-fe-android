package com.mobi.ripple.feature_app.feature_chat.domain.use_case

import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChat
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository


class GetChatUseCase(
    val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): SimpleChat {
        return repository.getChat(chatId)
    }
}