package com.mobi.ripple.feature_app.feature_chat.presentation.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.ChatModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.MessageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ChatState(
    var messagesFlow: Flow<PagingData<MessageModel>> = emptyFlow(),
    var chatModel: MutableState<ChatModel> = mutableStateOf(
        ChatModel("", "", null, emptyMap())
    )
)