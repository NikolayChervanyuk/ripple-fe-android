package com.mobi.ripple.core.domain.use_case.post

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.post.PostSimpleUser
import com.mobi.ripple.core.domain.repository.PostRepository

class GetSimpleUserUseCase(
    private val repository: PostRepository
) {

    suspend operator fun invoke(userId: String): Response<PostSimpleUser> {
        return repository.getSimpleUser(userId)
    }
}