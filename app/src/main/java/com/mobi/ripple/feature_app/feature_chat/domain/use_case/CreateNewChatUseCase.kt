package com.mobi.ripple.feature_app.feature_chat.domain.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChat
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository

class CreateNewChatUseCase(
    val repository: ChatRepository
) {
    suspend operator fun invoke(chatName: String?, participantIds: List<String>): Response<SimpleChat?> {
        return repository.createNewChat(chatName, participantIds)
    }
}
