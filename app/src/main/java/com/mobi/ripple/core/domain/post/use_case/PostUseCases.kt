package com.mobi.ripple.core.domain.post.use_case

import com.mobi.ripple.core.domain.post.use_case.comment.ChangeCommentLikeStateUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.DeleteCommentUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.EditCommentUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.UploadCommentUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.ChangeReplyLikeStateUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.DeleteReplyUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.EditReplyUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.GetCommentRepliesFlowUseCase
import com.mobi.ripple.core.domain.post.use_case.comment.reply.UploadReplyUseCase

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