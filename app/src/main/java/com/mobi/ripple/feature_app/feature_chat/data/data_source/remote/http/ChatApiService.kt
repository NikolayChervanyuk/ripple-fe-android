package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http

import com.mobi.ripple.core.data.common.SimpleUserResponse
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.GetChatResponse
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.GetMessageResponse
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.NewChatRequest
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.NewChatResponse
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.SimpleChatUserResponse

interface ChatApiService {
    suspend fun hasPendingMessages(): ApiResponse<Boolean>
    suspend fun findUsersLike(queryText: String) : ApiResponse<List<SimpleUserResponse>>
    suspend fun createNewChat(newChatRequest: NewChatRequest): ApiResponse<NewChatResponse>
    suspend fun getChatUser(userId: String): ApiResponse<SimpleUserResponse>
    suspend fun getChatParticipants(chatId: String): ApiResponse<List<SimpleChatUserResponse>>
    suspend fun getChats(page: Int): ApiResponse<List<GetChatResponse>>
    suspend fun getMessages(chatId: String, page:Int): ApiResponse<List<GetMessageResponse>>
}
