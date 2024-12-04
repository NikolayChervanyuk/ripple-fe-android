package com.mobi.ripple.feature_app.feature_chat.domain.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChatUser
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository

class GetChatUserUseCase(
    val repository: ChatRepository
) {
    suspend operator fun invoke(userId: String): Response<SimpleChatUser?> {
        return repository.getChatUser(userId)
    }
}