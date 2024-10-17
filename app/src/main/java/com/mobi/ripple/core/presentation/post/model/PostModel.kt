package com.mobi.ripple.core.presentation.post.model

import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.paging.PagingData
import androidx.paging.map
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.domain.post.model.Post
import com.mobi.ripple.core.domain.post.use_case.PostUseCases
import com.mobi.ripple.core.presentation.post.PostEvent
import com.mobi.ripple.core.presentation.post.components.comments.CommentAction
import com.mobi.ripple.core.util.BitmapUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import java.time.Instant

data class PostModel(
    var id: MutableState<String>,
    var creationDate: MutableState<Instant>,
    var lastModifiedDate: MutableState<Instant>,
    var authorId: MutableState<String>,
    var authorFullName: MutableState<String?>,
    var authorUsername: MutableState<String>,
    var isAuthorActive: MutableState<Boolean>,
    var authorPfp: MutableState<ImageBitmap?>,
    var postImage: MutableState<ImageBitmap?>,
    var caption: MutableState<String?>,
    var likesCount: MutableLongState,
    var liked: MutableState<Boolean>,
    var commentsCount: MutableLongState,
//    var personalPfp: MutableState<ImageBitmap?> = mutableStateOf(null),
    var postCommentsFlow: MutableState<Flow<PagingData<CommentModel>>> = mutableStateOf(emptyFlow()),
    var postUseCases: PostUseCases
) {

//    init {
//        personalPfp.value = GlobalAppManager.getProfilePicture()//authorPfp.value
//    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    suspend fun onEvent(event: PostEvent) {
        when (event) {
//            is PostEvent.InitPost -> {
//                personalPfp = event.postModel.authorPfp
//            }

            is PostEvent.PostLikeClicked -> {
                val oldState = this

                liked.value = !liked.value
                likesCount.longValue += if (liked.value) 1 else -1

                val response = postUseCases
                    .changePostLikeStateUseCase(id.value)
                if (response.isError) {
                    likesCount.longValue = oldState.likesCount.longValue
                    liked.value = oldState.liked.value
                } else {
                    _eventFlow.emit(UiEvent.StateMutated)
                }
            }

            is PostEvent.PostCommentsClicked -> {
                postCommentsFlow.value = postUseCases.getPostCommentsFlowUseCase(event.postId)
                    .map { pagingData -> pagingData.map { it.asCommentModel(postUseCases) } }
                _eventFlow.emit(UiEvent.ShowCommentsSection)

            }

            is PostEvent.PostShareClicked -> {
                _eventFlow.emit(UiEvent.ShowSnackBar("Sharing is unavailable at the moment"))
            }

            is PostEvent.CommentActionRequested -> {
                when (val action = event.commentAction) {
                    is CommentAction.NewComment -> {
                        val response = postUseCases
                            .uploadCommentUseCase(event.postId, action.text!!)
                        if (!response.isError) {
                            ++commentsCount.value
                            //update(this.copy(commentsCount = mutableStateOf(commentsCount + 1)))
//                            this.commentsCount += 1
                            _eventFlow.emit(
                                UiEvent.CommentOperationSuccessful("Comment uploaded")
                            )
                        } else {
                            _eventFlow.emit(
                                UiEvent.CommentOperationFailure("Comment failed to post, try again later")
                            )
                        }

                    }

                    is CommentAction.LikeComment -> {
                        val response = postUseCases
                            .changeCommentLikeStateUseCase(event.postId, action.commentId)
                        if (response.isError) {
                            _eventFlow.emit(
                                UiEvent.CommentOperationFailure("Failed to like comment, try again later")
                            )
                        } else {
                            _eventFlow.emit(
                                UiEvent.CommentOperationSuccessful(null)
                            )
                        }
                    }

                    is CommentAction.EditComment -> {
                        val response = postUseCases
                            .editCommentUseCase(event.postId, action.commentId, action.text!!)
                        if (!response.isError) {
                            _eventFlow.emit(
                                UiEvent.CommentOperationSuccessful("Edit successful")
                            )
                        }

                    }

                    is CommentAction.DeleteComment -> {
                        val response = postUseCases
                            .deleteCommentUseCase(event.postId, action.commentId)
                        if (!response.isError) {
                            --commentsCount.value
//                            update(this.copy(commentsCount = mutableLongStateOf(commentsCount.value - 1)))
                            _eventFlow.emit(
                                UiEvent.CommentOperationSuccessful("Comment deleted")
                            )
                        }

                    }

                    is CommentAction.LoadReplies -> {
                        //Replies flow is set in CommentBottomSheet
//                        commentRepliesFlow = postUseCases
//                            .getCommentRepliesFlowUseCase(event.postId, action.commentId)
//                            .map { pagingData -> pagingData.map { it.asReplyModel() } }
                    }

                    is CommentAction.NewReply -> {
                        val response = postUseCases.uploadReplyUseCase(
                            event.postId,
                            action.commentId,
                            action.text!!
                        )
                        if (!response.isError) {
                            _eventFlow.emit(
                                UiEvent.CommentOperationSuccessful("Reply posted")
                            )
                        }

                    }

                    is CommentAction.LikeReply -> {
                        val response = postUseCases
                            .changeReplyLikeStateUseCase(
                                event.postId,
                                action.commentId,
                                action.replyId
                            )
                        if (response.isError) {
                            _eventFlow.emit(
                                UiEvent.CommentOperationFailure("Service unavailable")
                            )
                        } else _eventFlow.emit(
                            UiEvent.CommentOperationSuccessful(null)
                        )
                    }

                    is CommentAction.EditReply -> {
                        val response = postUseCases
                            .editReplyUseCase(
                                event.postId,
                                action.commentId,
                                action.replyId,
                                action.text!!
                            )
                        if (!response.isError) {
                            _eventFlow.emit(
                                UiEvent.CommentOperationSuccessful("Reply edited successfully")
                            )
                        }

                    }

                    is CommentAction.DeleteReply -> {
                        val response = postUseCases
                            .deleteReplyUseCase(event.postId, action.commentId, action.replyId)
                        if (!response.isError) {
                            _eventFlow.emit(UiEvent.CommentOperationSuccessful("Reply deleted"))
                        }
                    }
                }
            }
        }
    }

//    private fun update(newPostModel: PostModel) {
//        this.id = newPostModel.id
//        this.creationDate = newPostModel.creationDate
//        this.lastModifiedDate = newPostModel.lastModifiedDate
//        this.authorId = newPostModel.authorId
//        this.authorFullName = newPostModel.authorFullName
//        this.authorUsername = newPostModel.authorUsername
//        this.isAuthorActive = newPostModel.isAuthorActive
//        this.authorPfp = newPostModel.authorPfp
//        this.postImage = newPostModel.postImage
//        this.caption = newPostModel.caption
//        this.likesCount = newPostModel.likesCount
//        this.liked = newPostModel.liked
//        this.commentsCount = newPostModel.commentsCount
////        this.personalPfp = newPostModel.personalPfp
////        this.postCommentsFlow = newPostModel.postCommentsFlow
////        this.commentRepliesFlow = newPostModel.commentRepliesFlow
//    }

    sealed class UiEvent {
        data object ShowCommentsSection : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
        data class CommentOperationSuccessful(
            val message: String?,
            val triggerRefresh: Boolean = true
        ) : UiEvent()

        data class CommentOperationFailure(val message: String) : UiEvent()

        data object StateMutated: UiEvent()
    }
}

fun Post.asPostModel(postUseCases: PostUseCases) = PostModel(
    id = mutableStateOf(id),
    creationDate = mutableStateOf(creationDate),
    lastModifiedDate = mutableStateOf(lastModifiedDate),
    authorId = mutableStateOf(authorId),
    authorFullName = mutableStateOf(authorFullName),
    authorUsername = mutableStateOf(authorUsername),
    isAuthorActive = mutableStateOf(isAuthorActive),
    authorPfp = mutableStateOf(authorPfp?.let {
        BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap()
    }),
    postImage = mutableStateOf(
        BitmapUtils.convertImageByteArrayToBitmap(postImage).asImageBitmap()
    ),
    caption = mutableStateOf(caption),
    likesCount = mutableLongStateOf(likesCount),
    liked = mutableStateOf(liked),
    commentsCount = mutableLongStateOf(commentsCount),
    postUseCases = postUseCases
)
