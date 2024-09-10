package com.mobi.ripple.core.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.mobi.ripple.core.domain.use_case.posts.PostsUseCases
import com.mobi.ripple.core.presentation.post.model.asPostModel
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
class PostsViewModel @Inject constructor(
    private val postsUseCases: PostsUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(PostsState())
    val state: StateFlow<PostsState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: PostsEvent) {
        when (event) {
            is PostsEvent.InitPostsList -> {
                viewModelScope.launch {
                    state.value
                        .postsFlowState.value = postsUseCases.getPostsUseCase(
                        event.startIndex,
                        event.authorId
                    ). map { pagingData ->
                        pagingData.map { it.asPostModel() }

                    }
                }
            }
        }
    }

    sealed class UiEvent {
//        data object : UiEvent()
    }
}