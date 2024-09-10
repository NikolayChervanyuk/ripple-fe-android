package com.mobi.ripple.core.presentation.posts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.mobi.ripple.core.presentation.components.DefaultHeader
import com.mobi.ripple.core.presentation.post.PostEvent
import com.mobi.ripple.core.presentation.post.PostItem
import com.mobi.ripple.core.presentation.post.PostViewModel
import kotlinx.serialization.Serializable

@Composable
fun PostsScreen(
    startIndex: Int,
    authorId: String,
    viewModel: PostsViewModel,
    navController: NavHostController,
    snackbarHost: SnackbarHostState
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    val postsLazyPagingItems = state.value
        .postsFlowState.value
        .collectAsLazyPagingItems()
    val currentViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "Current LocalViewModelStoreOwner is null"
    }
    LaunchedEffect(key1 = true) {
        viewModel.onEvent(PostsEvent.InitPostsList(startIndex, authorId))
        lazyListState.scrollToItem(startIndex)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DefaultHeader(
            onBackButtonClicked = { navController.popBackStack() },
            title = "Posts"
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = postsLazyPagingItems.itemCount,
                key = postsLazyPagingItems.itemKey { it.id },
            ) { index ->
                val item = postsLazyPagingItems[index]
                item?.let {
                    val postViewModel = viewModel(
                        modelClass = PostViewModel::class,
                        viewModelStoreOwner = currentViewModelStoreOwner
                    )
                    PostItem(
                        postModel = it,
                        viewModel = postViewModel,
                        snackbarHost = snackbarHost
                    )
                }
            }
        }
    }
}

@Serializable
data class PostsScreenRoute(
    val startIndex: Int,
    val authorId: String
    //TODO
)
