package com.mobi.ripple.core.domain.post.repository

import com.mobi.ripple.core.domain.common.Response

interface ReplyRepository {
    suspend fun uploadReply(postId: String, commentId: String, replyText: String): Response<Boolean?>

    suspend fun editReply(
        postId: String,
        commentId: String,
        replyId: String,
        newReplyText: String
    ): Response<Boolean?>
    suspend fun deleteReply(postId: String, commentId: String, replyId: String): Response<Boolean?>
    suspend fun likeOrUnlikeReply(postId: String, commentId: String, replyId: String): Response<Boolean?>
}