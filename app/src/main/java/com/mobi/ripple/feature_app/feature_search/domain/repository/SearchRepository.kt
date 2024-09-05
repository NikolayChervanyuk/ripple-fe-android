package com.mobi.ripple.feature_app.feature_search.domain.repository

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_app.feature_search.domain.model.SimpleUser

interface SearchRepository {

    suspend fun findUsersLikeUsernameUseCase(username: String): Response<List<SimpleUser>>
}