package com.mobi.ripple.core.domain.use_case.profile

data class ProfileUseCases(
    val getProfileInfoUseCase: GetProfileInfoUseCase,
    val getProfilePictureUseCase: GetProfilePictureUseCase,
    val getSimplePostsFlowUseCase: GetSimplePostsFlowUseCase,
    val changeFollowingStateUseCase: ChangeFollowingStateUseCase,
)
