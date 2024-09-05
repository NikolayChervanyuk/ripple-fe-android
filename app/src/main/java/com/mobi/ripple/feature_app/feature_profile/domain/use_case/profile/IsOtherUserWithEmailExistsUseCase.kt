package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.ProfileRepository
import com.mobi.ripple.feature_app.feature_profile.domain.repository.PersonalProfileRepository

class IsOtherUserWithEmailExistsUseCase(
    private val repository: PersonalProfileRepository
) {
    suspend operator fun invoke(email: String): Response<Boolean?> {
        return repository.isOtherUserWithEmailExists(email)
    }
}