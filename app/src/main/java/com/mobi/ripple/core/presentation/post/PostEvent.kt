package com.mobi.ripple.core.presentation.post

import com.mobi.ripple.core.presentation.post.components.comments.CommentAction
import com.mobi.ripple.core.presentation.post.model.PostModel

sealed class PostEvent {
    data class InitPost(val postModel: PostModel) : PostEvent()
    data class PostLikeClicked(val postId: String) : PostEvent()
    data class PostCommentsClicked(val postId: String) : PostEvent()
    data class PostShareClicked(val postId: String) : PostEvent()
    data class CommentActionRequested(
        val postId: String,
        val commentAction: CommentAction
    ) : PostEvent()
//    data class UploadCommentRequested(val postId: String, val commentText: String) : PostEvent()
//    data class CommentLiked(val postId: String, val commentId: String) : PostEvent()
}