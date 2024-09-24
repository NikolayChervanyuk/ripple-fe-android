package com.mobi.ripple.feature_app.feature_search.domain.repository

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_search.domain.model.SearchSimpleUser

interface SearchRepository {

    suspend fun findUsersLikeUsernameUseCase(username: String): Response<List<SearchSimpleUser>>
}