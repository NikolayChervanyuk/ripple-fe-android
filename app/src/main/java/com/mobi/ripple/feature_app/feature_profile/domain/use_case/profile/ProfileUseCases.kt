package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

data class ProfileUseCases(
    val getProfilePictureUseCase: GetProfilePictureUseCase,
    val getProfileInfoUseCase: GetProfileInfoUseCase,
    val getSimplePostsUseCase: GetSimplePostsUseCase,
    val uploadPfpUseCase: UploadPfpUseCase,
    val deletePfpUseCase: DeletePfpUseCase,
    val isOtherUserWithUsernameExists: IsOtherUserWithUsernameExistsUseCase,
    val isOtherUserWithEmailExists: IsOtherUserWithEmailExistsUseCase,
    val editProfileInfoUseCase: EditProfileInfoUseCase
)
