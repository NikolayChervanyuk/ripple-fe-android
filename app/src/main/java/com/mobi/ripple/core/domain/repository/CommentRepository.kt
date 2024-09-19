package com.mobi.ripple.core.domain.repository

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.post.Reply
import com.mobi.ripple.core.domain.model.post.UploadComment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun uploadComment(postId: String, comment: UploadComment): Response<Boolean?>
    suspend fun likeOrUnlikeComment(postId: String, commentId: String): Response<Boolean?>
    suspend fun editComment(
        postId: String,
        commentId: String,
        newCommentText: String
    ): Response<Boolean?>

    suspend fun deleteComment(postId: String, commentId: String): Response<Boolean?>

    suspend fun getCommentRepliesFlow(postId: String, commentId: String): Flow<PagingData<Reply>>

}