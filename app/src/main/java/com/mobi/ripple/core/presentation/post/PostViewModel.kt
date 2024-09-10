package com.mobi.ripple.core.presentation.post

import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.domain.use_case.post.PostUseCases
import com.mobi.ripple.core.presentation.post.model.asCommentModel
import com.mobi.ripple.core.presentation.post.model.asPostSimpleUserModel
import com.mobi.ripple.core.util.BitmapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCases: PostUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(PostState())
    val state: StateFlow<PostState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            _state.value.personalPfp.value = GlobalAppManager.getProfilePicture()?.let {
                BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap()
            }
        }
    }

    fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.InitPost -> {
                viewModelScope.launch {
//                    val postResponse = postUseCases.getPostUseCase(event.postModel.id)
//                    if (!postResponse.isError) {
//                        _state.value.postModel.value = postResponse.content!!.asPostModel()
                    _state.value.postModel.value = event.postModel
                    val authorResponse = postUseCases.getSimpleUserUseCase(
                        _state.value.postModel.value.authorId
                    )
                    if (!authorResponse.isError) {
                        launch {
                            _state.value
                                .postSimpleUserModel.value =
                                authorResponse.content!!.asPostSimpleUserModel()
                        }
                    }
                    //}
                }
            }

            is PostEvent.PostLikeClicked -> {
                viewModelScope.launch {
                    val response = postUseCases
                        .changePostLikeStateUseCase(
                            state.value
                                .postModel.value
                                .id
                        )
                    if (!response.isError) {
                        val newPostModel = _state.value
                            .postModel.value
                        newPostModel.liked = !newPostModel.liked
                        newPostModel.likesCount += if (newPostModel.liked) 1 else -1

                        _state.value
                            .postModel.value = newPostModel
                    }
                }
            }

            is PostEvent.PostCommentsClicked -> {
                viewModelScope.launch {
                    _state.value
                        .postCommentsFlow = postUseCases.getPostCommentsFlowUseCase(event.postId)
                        .map { pagingData -> pagingData.map { it.asCommentModel() } }
                    _eventFlow.emit(UiEvent.ShowCommentsSection)
                }
            }

            is PostEvent.PostShareClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowSnackBar("Sharing is unavailable at the moment"))
                }
            }

            is PostEvent.UploadCommentRequested -> {
                viewModelScope.launch {
                    val response = postUseCases
                        .uploadCommentUseCase(event.postId, event.commentText)
                    if (!response.isError) {
                        _eventFlow.emit(UiEvent.UploadCommentSuccessful)
                    } else {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar("Comment failed to post, try again later")
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data object ShowCommentsSection : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
        data object UploadCommentSuccessful : UiEvent()
    }
}
