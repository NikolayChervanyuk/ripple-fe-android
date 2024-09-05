package com.mobi.ripple.feature_app.feature_search.data.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiRequest
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse
import com.mobi.ripple.feature_app.feature_search.data.data_source.remote.dto.SimpleUserResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SearchApiServiceImpl(
    val client: HttpClient
): SearchApiService {
    override suspend fun findUsersLikeUsername(
        username: String
    ): ApiResponse<List<SimpleUserResponse>> = ApiRequest<List<SimpleUserResponse>> {
        client.get(AppUrls.SearchUrls.searchUsersLike(username))
    }.sendRequest()
}