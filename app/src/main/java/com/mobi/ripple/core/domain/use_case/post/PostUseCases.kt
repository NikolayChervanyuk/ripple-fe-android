package com.mobi.ripple.core.domain.use_case.post

data class PostUseCases(
    val getPostUseCase: GetPostUseCase,
    val getSimpleUserUseCase: GetSimpleUserUseCase,
    val changePostLikeStateUseCase: ChangePostLikeStateUseCase,
    val getPostCommentsFlowUseCase: GetPostCommentsFlowUseCase,
    val uploadCommentUseCase: UploadCommentUseCase
)