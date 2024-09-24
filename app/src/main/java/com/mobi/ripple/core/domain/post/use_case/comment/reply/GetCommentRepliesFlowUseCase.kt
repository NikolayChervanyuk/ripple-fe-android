package com.mobi.ripple.core.domain.post.use_case.comment.reply

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.post.model.Reply
import com.mobi.ripple.core.domain.post.repository.CommentRepository
import kotlinx.coroutines.flow.Flow

class GetCommentRepliesFlowUseCase(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: String, commentId: String): Flow<PagingData<Reply>> {
        return repository.getCommentRepliesFlow(postId, commentId)
    }
}