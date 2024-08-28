package com.mobi.ripple.feature_app.feature_profile.domain.use_case

data class ProfileUseCases(
    val getProfilePictureUseCase: GetProfilePictureUseCase,
    val getProfileInfoUseCase: GetProfileInfoUseCase,
    val getSimplePostsUseCase: GetSimplePostsUseCase
)
