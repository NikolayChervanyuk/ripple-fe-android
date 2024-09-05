package com.mobi.ripple.feature_app.feature_search.domain.use_case

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_app.feature_search.domain.model.SimpleUser
import com.mobi.ripple.feature_app.feature_search.domain.repository.SearchRepository

data class FindUsersLikeUsernameUseCase(
    val repository: SearchRepository
){

    suspend operator fun invoke(username: String): Response<List<SimpleUser>> {
        return repository.findUsersLikeUsernameUseCase(username)
    }
}
