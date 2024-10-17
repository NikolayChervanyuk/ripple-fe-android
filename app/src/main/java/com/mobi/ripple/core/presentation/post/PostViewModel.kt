//package com.mobi.ripple.core.presentation.post
//
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.paging.map
//import com.mobi.ripple.GlobalAppManager
//import com.mobi.ripple.core.domain.post.use_case.PostUseCases
//import com.mobi.ripple.core.presentation.post.components.comments.CommentAction
//import com.mobi.ripple.core.presentation.post.model.asCommentModel
//import com.mobi.ripple.core.presentation.post.model.asReplyModel
//import com.mobi.ripple.core.util.BitmapUtils
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class PostViewModel @Inject constructor(
//    private val postUseCases: PostUseCases
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(PostState())
//    val state: StateFlow<PostState> = _state.asStateFlow()
//
//    private val _eventFlow = MutableSharedFlow<UiEvent>()
//    val eventFlow = _eventFlow.asSharedFlow()
//
//    init {
//        viewModelScope.launch {
//            _state.value.personalPfp.value = GlobalAppManager.getProfilePicture()?.let {
//                BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap()
//            }
//        }
//    }
//
//    fun onEvent(event: PostEvent) {
//        when (event) {
//            is PostEvent.InitPost -> {
//                viewModelScope.launch {
//                    _state.value.postModel.value = event.postModel
//                }
//            }
//
//            is PostEvent.PostLikeClicked -> {
//                viewModelScope.launch {
//                    val oldModel = _state.value.postModel.value
//                    launch {
//                        val newPostModel = _state.value
//                            .postModel.value
//                        newPostModel.liked = !newPostModel.liked
//                        newPostModel.likesCount += if (newPostModel.liked) 1 else -1
//
//                        _state.value
//                            .postModel.value = newPostModel
//                    }
//                    val response = postUseCases
//                        .changePostLikeStateUseCase(
//                            state.value
//                                .postModel.value
//                                .id
//                        )
//                    if (response.isError) {
//                        _state.value
//                            .postModel.value = oldModel
//                    }
//                }
//            }
//
//            is PostEvent.PostCommentsClicked -> {
//                viewModelScope.launch {
//                    _state.value
//                        .postCommentsFlow = postUseCases.getPostCommentsFlowUseCase(event.postId)
//                        .map { pagingData -> pagingData.map { it.asCommentModel() } }
//                    _eventFlow.emit(UiEvent.ShowCommentsSection)
//                }
//            }
//
//            is PostEvent.PostShareClicked -> {
//                viewModelScope.launch {
//                    _eventFlow.emit(UiEvent.ShowSnackBar("Sharing is unavailable at the moment"))
//                }
//            }
//
//            is PostEvent.CommentActionRequested -> {
//                when (val action = event.commentAction) {
//                    is CommentAction.NewComment -> {
//                        viewModelScope.launch {
//                            val response = postUseCases
//                                .uploadCommentUseCase(event.postId, action.text!!)
//                            if (!response.isError) {
//                                _state.value.postModel.value = with(_state.value.postModel.value) {
//                                    this.copy(commentsCount = commentsCount + 1)
//                                }
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationSuccessful("Comment uploaded")
//                                )
//                            } else {
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationFailure("Comment failed to post, try again later")
//                                )
//                            }
//                        }
//                    }
//
//                    is CommentAction.LikeComment -> {
//                        viewModelScope.launch {
//                            val response = postUseCases
//                                .changeCommentLikeStateUseCase(event.postId, action.commentId)
//                            if (response.isError) {
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationFailure("Failed to like comment, try again later")
//                                )
//                            } else {
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationSuccessful(null)
//                                )
//                            }
//                        }
//                    }
//
//                    is CommentAction.EditComment -> {
//                        viewModelScope.launch {
//                            val response = postUseCases
//                                .editCommentUseCase(event.postId, action.commentId, action.text!!)
//                            if (!response.isError) {
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationSuccessful("Edit successful")
//                                )
//                            }
//                        }
//                    }
//
//                    is CommentAction.DeleteComment -> {
//                        viewModelScope.launch {
//                            val response = postUseCases
//                                .deleteCommentUseCase(event.postId, action.commentId)
//                            if (!response.isError) {
//                                _state.value.postModel.value = with(_state.value.postModel.value) {
//                                    this.copy(commentsCount = commentsCount - 1)
//                                }
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationSuccessful("Comment deleted")
//                                )
//                            }
//                        }
//                    }
//
//                    is CommentAction.LoadReplies -> {
//                        viewModelScope.launch {
//                            val response = postUseCases
//                                .getCommentRepliesFlowUseCase(event.postId, action.commentId)
//                                .map { pagingData -> pagingData.map { it.asReplyModel() } }
//                            _state.value.commentRepliesFlow = response
//                        }
//                    }
//
//                    is CommentAction.NewReply -> {
//                        viewModelScope.launch {
//                            val response = postUseCases.uploadReplyUseCase(
//                                event.postId,
//                                action.commentId,
//                                action.text!!
//                            )
//                            if (!response.isError) {
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationSuccessful("Reply posted")
//                                )
//                            }
//                        }
//                    }
//
//                    is CommentAction.LikeReply -> {
//                        viewModelScope.launch {
//                            val response = postUseCases
//                                .changeReplyLikeStateUseCase(
//                                    event.postId,
//                                    action.commentId,
//                                    action.replyId
//                                )
//                            if (response.isError) {
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationFailure("Service unavailable")
//                                )
//                            } else _eventFlow.emit(
//                                UiEvent.CommentOperationSuccessful(null)
//                            )
//                        }
//                    }
//
//                    is CommentAction.EditReply -> {
//                        viewModelScope.launch {
//                            val response = postUseCases
//                                .editReplyUseCase(
//                                    event.postId,
//                                    action.commentId,
//                                    action.replyId,
//                                    action.text!!
//                                )
//                            if (!response.isError) {
//                                _eventFlow.emit(
//                                    UiEvent.CommentOperationSuccessful("Reply edited successfully")
//                                )
//                            }
//                        }
//                    }
//
//                    is CommentAction.DeleteReply -> {
//                        viewModelScope.launch {
//                            val response = postUseCases
//                                .deleteReplyUseCase(event.postId, action.commentId, action.replyId)
//                            if (!response.isError) {
//                                _eventFlow.emit(UiEvent.CommentOperationSuccessful("Reply deleted"))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
////    sealed class UiEvent {
////        data object ShowCommentsSection : UiEvent()
////        data class ShowSnackBar(val message: String) : UiEvent()
////        data class CommentOperationSuccessful(
////            val message: String?,
////            val triggerRefresh: Boolean = true
////        ) : UiEvent()
////
////        data class CommentOperationFailure(val message: String) : UiEvent()
////    }
//}