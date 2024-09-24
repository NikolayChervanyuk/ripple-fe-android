package com.mobi.ripple.core.presentation.post

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.paging.PagingData
import com.mobi.ripple.core.presentation.post.model.CommentModel
import com.mobi.ripple.core.presentation.post.model.PostModel
import com.mobi.ripple.core.presentation.post.model.PostSimpleUserModel
import com.mobi.ripple.core.presentation.post.model.ReplyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.Instant

data class PostState(
    var personalPfp: MutableState<ImageBitmap?> = mutableStateOf(null),
    var postSimpleUserModel: MutableState<PostSimpleUserModel> = mutableStateOf(
        PostSimpleUserModel(
            id = "",
            fullName = null,
            username = "",
            isActive = false,
            profilePicture = null
        )
    ),
    var postModel: MutableState<PostModel> = mutableStateOf(
        PostModel(
            id = "",
            creationDate = Instant.MIN,
            lastModifiedDate = Instant.MIN,
            authorId = "",
            authorFullName = null,
            authorUsername = "",
            authorPfp = null,
            isAuthorActive = false,
            postImage = null,
            caption = null,
            likesCount = 0L,
            liked = false,
            commentsCount = 0L
        )
    ),
    var postCommentsFlow: Flow<PagingData<CommentModel>> = emptyFlow(),
    var commentRepliesFlow: Flow<PagingData<ReplyModel>> = emptyFlow()
)