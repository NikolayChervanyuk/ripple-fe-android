package com.mobi.ripple.core.domain.post.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.PostSimpleUser
import com.mobi.ripple.core.domain.post.repository.PostRepository

class GetSimpleUserUseCase(
    private val repository: PostRepository
) {

    suspend operator fun invoke(userId: String): Response<PostSimpleUser> {
        return repository.getSimpleUser(userId)
    }
}