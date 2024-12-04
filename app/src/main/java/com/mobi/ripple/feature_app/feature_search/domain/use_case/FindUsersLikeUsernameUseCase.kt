package com.mobi.ripple.feature_app.feature_search.domain.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_search.domain.model.SearchSimpleUser
import com.mobi.ripple.feature_app.feature_search.domain.repository.SearchRepository

data class FindUsersLikeUsernameUseCase(
    val repository: SearchRepository
){

    suspend operator fun invoke(username: String): Response<List<SearchSimpleUser>> {
        return repository.findUsersLikeUsernameUseCase(username)
    }
}
