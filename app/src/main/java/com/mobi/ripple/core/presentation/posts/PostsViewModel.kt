package com.mobi.ripple.core.presentation.posts

//import com.mobi.ripple.core.presentation.post.PostViewModel
//import com.mobi.ripple.core.presentation.posts.model.PostItemModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.mobi.ripple.core.config.ConstraintValues.Companion.POSTS_PAGE_SIZE
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.post.data_source.remote.PostApiService
import com.mobi.ripple.core.data.post.data_source.remote.PostRemoteMediator
import com.mobi.ripple.core.domain.post.model.Post
import com.mobi.ripple.core.domain.post.use_case.PostUseCases
import com.mobi.ripple.core.domain.post.use_case.posts.PostsUseCases
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
    private val postsUseCases: PostsUseCases,
    private val postUseCases: PostUseCases,
    private val database: AppDatabase,
    private val apiService: PostApiService
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
                            it.asPostModel(postUseCases)
                        }
                    }.cachedIn(viewModelScope)
                }
            }
        }
    }

//    suspend fun getFirstCachedPost(): Post {
//        return postsUseCases.getFirstCachedPostUseCase()
//        //return _state.value.firstCachedPost
//    }

    @OptIn(ExperimentalPagingApi::class)
    fun initPosts(startItemIndex: Int, authorId: String) {
        viewModelScope.launch {
            database.postDao.clearAll()
            state.value
                .postsFlowState =
                Pager(
                    initialKey = startItemIndex,
                    config = PagingConfig(
                        pageSize = POSTS_PAGE_SIZE,
                        prefetchDistance = POSTS_PAGE_SIZE,
                        maxSize = 3 * POSTS_PAGE_SIZE,
                        jumpThreshold = 3,
                        enablePlaceholders = true
                    ),
                    remoteMediator = PostRemoteMediator(
                        appDb = database,
                        apiService = apiService,
                        authorId = authorId,
                        startItemIndex = startItemIndex
                    ),
                    pagingSourceFactory = { database.postDao.pagingSource() }
                ).flow.map { pagingData ->
                    pagingData.map { it.asPost().asPostModel(postUseCases) }
                }.cachedIn(viewModelScope)
        }
    }

//    private fun getPostViewModelInstance(): PostViewModel {
//        return PostViewModelFactory(postUseCases).create(PostViewModel::class.java)
//    }

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