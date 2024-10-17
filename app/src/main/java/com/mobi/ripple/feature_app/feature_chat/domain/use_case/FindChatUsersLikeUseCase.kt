package com.mobi.ripple.feature_app.feature_chat.domain.use_case

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChatUser
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class FindChatUsersLikeUseCase(
    val repository: ChatRepository
) {

    suspend operator fun invoke(queryText: String): Response<List<SimpleChatUser>?> {
        return repository.findUsersLike(queryText)
    }
}