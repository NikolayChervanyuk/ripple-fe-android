package com.mobi.ripple.core.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.mobi.ripple.core.domain.post.use_case.PostUseCases
import com.mobi.ripple.core.domain.post.use_case.posts.PostsUseCases
import com.mobi.ripple.core.presentation.post.PostViewModel
import com.mobi.ripple.core.presentation.post.model.asPostModel
import com.mobi.ripple.core.presentation.posts.model.PostItemModel
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
    private val postsUseCases: PostsUseCases,
    private val postUseCases: PostUseCases
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
                        .postsFlowState = postsUseCases.getPostsUseCase(
                        event.startItemIndex,
                        event.authorId
                    ).map { pagingData ->
                        pagingData.map {
                            PostItemModel(
                                it.asPostModel(),
                                getPostViewModelInstance() //FIXME: high memory consumption causes crashes
                            )
                        }
                    }.cachedIn(viewModelScope)
                }
            }
        }
    }

    private fun getPostViewModelInstance(): PostViewModel {
        return PostViewModelFactory(postUseCases).create(PostViewModel::class.java)
    }

    sealed class UiEvent {}

    private class PostViewModelFactory(
        private val postUseCases: PostUseCases
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass
                .getConstructor(PostUseCases::class.java)
                .newInstance(postUseCases)
        }
    }
}