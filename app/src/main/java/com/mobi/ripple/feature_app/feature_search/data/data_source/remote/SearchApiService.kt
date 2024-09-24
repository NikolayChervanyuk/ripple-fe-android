package com.mobi.ripple.feature_app.feature_search.data.data_source.remote

import com.mobi.ripple.core.data.common.SimpleUserResponse
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse

interface SearchApiService {
    suspend fun findUsersLikeUsername(username: String): ApiResponse<List<SimpleUserResponse>>
}