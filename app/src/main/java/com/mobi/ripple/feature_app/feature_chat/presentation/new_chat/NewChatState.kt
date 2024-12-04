package com.mobi.ripple.feature_app.feature_chat.presentation.new_chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mobi.ripple.core.presentation.components.SimpleUserItemModel
import com.mobi.ripple.feature_app.feature_chat.presentation.model.SimpleChatUserModel

data class NewChatState(
    val foundUsersList: MutableState<List<SimpleUserItemModel>> = mutableStateOf(emptyList()),
    val addedParticipantsList: SnapshotStateList<SimpleChatUserModel> = mutableStateListOf(),
    val chatName: MutableState<String> = mutableStateOf("")
)