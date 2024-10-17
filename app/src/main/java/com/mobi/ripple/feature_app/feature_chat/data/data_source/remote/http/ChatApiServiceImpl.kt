package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.common.SimpleUserResponse
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.GetChatResponse
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.GetMessageResponse
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.NewChatRequest
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.NewChatResponse
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.SimpleChatUserResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ChatApiServiceImpl(
    val client: HttpClient
): ChatApiService {
    override suspend fun hasPendingMessages(): ApiResponse<Boolean> = ApiRequest<Boolean> {
        client.get(AppUrls.ChatUrls.HAS_PENDING_MESSAGES)
    }.sendRequest()

    override suspend fun findUsersLike(
        queryText: String
    ): ApiResponse<List<SimpleUserResponse>> = ApiRequest<List<SimpleUserResponse>> {
        client.get(AppUrls.ChatUrls.findSimpleUsersLike(queryText, queryText))
    }.sendRequest()

    override suspend fun createNewChat(
        newChatRequest: NewChatRequest
    ): ApiResponse<NewChatResponse> = ApiRequest<NewChatResponse> {
        client.post(AppUrls.ChatUrls.CREATE_CHAT) {
            setBody(newChatRequest)
        }
    }.sendRequest()

    override suspend fun getChatUser(
        userId: String
    ): ApiResponse<SimpleUserResponse> = ApiRequest<SimpleUserResponse> {
        client.get(AppUrls.ChatUrls.getSimpleChatUser(userId))
    }.sendRequest()

    override suspend fun getChatParticipants(
        chatId: String
    ): ApiResponse<List<SimpleChatUserResponse>> = ApiRequest<List<SimpleChatUserResponse>> {
        client.get(AppUrls.ChatUrls.getChatParticipants(chatId))
    }.sendRequest()

    override suspend fun getChats(
        page: Int
    ): ApiResponse<List<GetChatResponse>> = ApiRequest<List<GetChatResponse>> {
        client.get(AppUrls.ChatUrls.getChats(page))
    }.sendRequest()

    override suspend fun getMessages(
        chatId: String,
        page:Int
    ): ApiResponse<List<GetMessageResponse>> = ApiRequest<List<GetMessageResponse>> {
        client.get(AppUrls.ChatUrls.getChatMessages(chatId, page))
    }.sendRequest()
}