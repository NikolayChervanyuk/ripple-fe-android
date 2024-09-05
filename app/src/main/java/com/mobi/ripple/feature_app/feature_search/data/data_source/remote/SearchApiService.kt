package com.mobi.ripple.feature_app.feature_search.data.data_source.remote

import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse
import com.mobi.ripple.feature_app.feature_search.data.data_source.remote.dto.SimpleUserResponse

interface SearchApiService {
    suspend fun findUsersLikeUsername(username: String): ApiResponse<List<SimpleUserResponse>>
}