package com.mobi.ripple.core.domain.post.repository

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.Reply
import kotlinx.coroutines.flow.Flow

interface ReplyRepository {

    suspend fun getRepliesFlow(postId: String, commentId: String): Flow<PagingData<Reply>>

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