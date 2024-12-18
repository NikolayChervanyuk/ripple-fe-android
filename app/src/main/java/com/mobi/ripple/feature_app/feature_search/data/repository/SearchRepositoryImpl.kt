package com.mobi.ripple.feature_app.feature_search.data.repository

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_search.data.data_source.remote.SearchApiService
import com.mobi.ripple.feature_app.feature_search.domain.model.SearchSimpleUser
import com.mobi.ripple.feature_app.feature_search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val apiService: SearchApiService
) : SearchRepository {
    override suspend fun findUsersLikeUsernameUseCase(username: String): Response<List<SearchSimpleUser>> {
        val apiResponse = apiService.findUsersLikeUsername(username.lowercase())

        return apiResponse.toResponse(
            apiResponse.content?.map { it.asSimpleUser() }
        )
    }
}