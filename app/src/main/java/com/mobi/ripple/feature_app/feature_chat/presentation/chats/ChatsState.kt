package com.mobi.ripple.feature_app.feature_chat.presentation.chats

import androidx.paging.PagingData
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.model.SimpleChatModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ChatsState(
    var chatsFlow: Flow<PagingData<SimpleChatModel>> = emptyFlow()
)