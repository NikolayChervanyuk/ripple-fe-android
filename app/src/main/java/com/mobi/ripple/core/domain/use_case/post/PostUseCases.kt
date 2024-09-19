package com.mobi.ripple.core.domain.use_case.post

import com.mobi.ripple.core.domain.use_case.post.comment.ChangeCommentLikeStateUseCase
import com.mobi.ripple.core.domain.use_case.post.comment.DeleteCommentUseCase
import com.mobi.ripple.core.domain.use_case.post.comment.EditCommentUseCase
import com.mobi.ripple.core.domain.use_case.post.comment.UploadCommentUseCase
import com.mobi.ripple.core.domain.use_case.post.comment.reply.ChangeReplyLikeStateUseCase
import com.mobi.ripple.core.domain.use_case.post.comment.reply.DeleteReplyUseCase
import com.mobi.ripple.core.domain.use_case.post.comment.reply.EditReplyUseCase
import com.mobi.ripple.core.domain.use_case.post.comment.reply.GetCommentRepliesFlowUseCase
import com.mobi.ripple.core.domain.use_case.post.comment.reply.UploadReplyUseCase

data class PostUseCases(
    val getPostUseCase: GetPostUseCase,
    val getSimpleUserUseCase: GetSimpleUserUseCase,
    val changePostLikeStateUseCase: ChangePostLikeStateUseCase,
    val getPostCommentsFlowUseCase: GetPostCommentsFlowUseCase,

    val uploadCommentUseCase: UploadCommentUseCase,
    val changeCommentLikeStateUseCase: ChangeCommentLikeStateUseCase,
    val editCommentUseCase: EditCommentUseCase,
    val deleteCommentUseCase: DeleteCommentUseCase,
    val getCommentRepliesFlowUseCase: GetCommentRepliesFlowUseCase,

    val uploadReplyUseCase: UploadReplyUseCase,
    val changeReplyLikeStateUseCase: ChangeReplyLikeStateUseCase,
    val editReplyUseCase: EditReplyUseCase,
    val deleteReplyUseCase: DeleteReplyUseCase
)