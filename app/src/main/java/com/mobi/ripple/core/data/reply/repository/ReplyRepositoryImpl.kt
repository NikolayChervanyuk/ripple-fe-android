package com.mobi.ripple.core.data.reply.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.reply.data_source.pager.RepliesPager
import com.mobi.ripple.core.data.reply.data_source.remote.ReplyApiService
import com.mobi.ripple.core.data.reply.data_source.remote.dto.EditReplyRequest
import com.mobi.ripple.core.data.reply.data_source.remote.dto.UploadReplyRequest
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.post.model.Reply
import com.mobi.ripple.core.domain.post.repository.ReplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReplyRepositoryImpl(
    private val replyApiService: ReplyApiService,
    private val database: AppDatabase
) : ReplyRepository {
    override suspend fun getRepliesFlow(
        postId: String,
        commentId: String
    ): Flow<PagingData<Reply>> {
        return RepliesPager(
            appDb = database,
            apiService = replyApiService,
            postId = postId,
            commentId = commentId
        ).getPager().flow.map { pagingData ->
            pagingData.map { it.asReply() }
        }
    }

    override suspend fun uploadReply(
        postId: String,
        commentId: String,
        replyText: String
    ): Response<Boolean?> {
        val apiResponse = replyApiService
            .uploadReply(postId, commentId, UploadReplyRequest(replyText))

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun editReply(
        postId: String,
        commentId: String,
        replyId: String,
        newReplyText: String
    ): Response<Boolean?> {
        val apiResponse = replyApiService
            .editReply(postId, commentId, replyId, EditReplyRequest(newReplyText))

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun deleteReply(
        postId: String,
        commentId: String,
        replyId: String
    ): Response<Boolean?> {
        val apiResponse = replyApiService.deleteReply(postId, commentId, replyId)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun likeOrUnlikeReply(
        postId: String,
        commentId: String,
        replyId: String
    ): Response<Boolean?> {
        val apiResponse = replyApiService.likeOrUnlikeReply(postId, commentId, replyId)

        return apiResponse.toResponse(apiResponse.content)
    }
}