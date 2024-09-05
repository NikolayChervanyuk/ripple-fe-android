package com.mobi.ripple.feature_app.feature_search.data.repository

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_app.feature_search.data.data_source.remote.SearchApiService
import com.mobi.ripple.feature_app.feature_search.domain.model.SimpleUser
import com.mobi.ripple.feature_app.feature_search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val apiService: SearchApiService
) : SearchRepository {
    override suspend fun findUsersLikeUsernameUseCase(username: String): Response<List<SimpleUser>> {
        val apiResponse = apiService.findUsersLikeUsername(username.lowercase())

        return apiResponse.toResponse(
            apiResponse.content?.map { it.asSimpleUser() }
        )
    }
}