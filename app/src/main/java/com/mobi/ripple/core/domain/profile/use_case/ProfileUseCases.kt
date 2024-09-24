package com.mobi.ripple.core.domain.profile.use_case

data class ProfileUseCases(
    val getProfileInfoUseCase: GetProfileInfoUseCase,
    val getProfilePictureUseCase: GetProfilePictureUseCase,
    val getSimplePostsFlowUseCase: GetSimplePostsFlowUseCase,
    val changeFollowingStateUseCase: ChangeFollowingStateUseCase,
)
