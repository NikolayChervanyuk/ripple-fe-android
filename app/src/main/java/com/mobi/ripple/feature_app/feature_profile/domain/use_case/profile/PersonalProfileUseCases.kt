package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.use_case.profile.GetProfileInfoUseCase
import com.mobi.ripple.core.domain.use_case.profile.GetProfilePictureUseCase
import com.mobi.ripple.core.domain.use_case.profile.GetSimplePostsUseCase

data class PersonalProfileUseCases(
    val getProfilePictureUseCase: GetProfilePictureUseCase,
    val getProfileInfoUseCase: GetProfileInfoUseCase,
    val getSimplePostsUseCase: GetSimplePostsUseCase,
    val uploadPfpUseCase: UploadPfpUseCase,
    val deletePfpUseCase: DeletePfpUseCase,
    val isOtherUserWithUsernameExists: IsOtherUserWithUsernameExistsUseCase,
    val isOtherUserWithEmailExists: IsOtherUserWithEmailExistsUseCase,
    val editProfileInfoUseCase: EditProfileInfoUseCase,
    val uploadPostUseCase: UploadPostUseCase
)
