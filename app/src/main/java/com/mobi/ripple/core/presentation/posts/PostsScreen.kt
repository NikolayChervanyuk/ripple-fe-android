package com.mobi.ripple.core.presentation.posts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.mobi.ripple.core.config.ConstraintValues.Companion.POSTS_PAGE_SIZE
import com.mobi.ripple.core.presentation.components.CircularProgressIndicatorRow
import com.mobi.ripple.core.presentation.components.DefaultActionBar
import com.mobi.ripple.core.presentation.post.PostItem
import com.mobi.ripple.core.presentation.post.model.PostModel
import kotlinx.serialization.Serializable

@Composable
fun PostsScreen(
    startItemIndex: Int,
    startItemId: String,
    authorId: String,
    viewModel: PostsViewModel,
    navController: NavHostController,
    snackbarHost: SnackbarHostState
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    val postsLazyPagingItems = state.value
        .postsFlowState
        .collectAsLazyPagingItems()

    val mutatedStates = remember{ mutableMapOf<String, PostModel>() }

    val hasBeenInit = remember { mutableStateOf(false) }
    val shouldRequestInitialScroll = remember { mutableStateOf(true) }

//    val shouldRequestPrependScroll = remember { mutableStateOf(startItemIndex) }
//    val prependingStarted = remember { mutableStateOf(false) }
//    val firstVisibleItemIdBeforePrepending = remember { mutableStateOf("") }
//    val requestItemIndex = remember { mutableStateOf(startItemIndex) }
//    val requestItemId = remember { mutableStateOf(startItemId) }

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(PostsEvent.InitPostsList(startItemIndex, authorId))
    }

    LaunchedEffect(key1 = postsLazyPagingItems.loadState.isIdle) {
        if (postsLazyPagingItems.loadState.isIdle) {
            //during composition, the load state will be idle, we should request scroll until
            //the actual initial loading operations complete and loadState becomes idle again.
            if (!hasBeenInit.value) {
                hasBeenInit.value = true
                return@LaunchedEffect
            } else if (shouldRequestInitialScroll.value) {
                shouldRequestInitialScroll.value = false
            } else {
//                lazyListState
//                    .requestScrollToItem(lazyListState.firstVisibleItemIndex)
            }
        }
    }

    LaunchedEffect(key1 = postsLazyPagingItems.itemCount) {
        if(startItemIndex < POSTS_PAGE_SIZE &&
            postsLazyPagingItems.itemCount == POSTS_PAGE_SIZE &&
            shouldRequestInitialScroll.value
            ) {
            lazyListState.requestScrollToItem(startItemIndex)
            shouldRequestInitialScroll.value = false
        }
        // Keep scrolling to the requested item until initial loading completes.
        // This would solve the problem of prepending up until the first page
        // by ultimately making scroll state's index > the prefetch distance
        if (shouldRequestInitialScroll.value) {
            var isFound = false
            for ((index, item) in postsLazyPagingItems.itemSnapshotList.withIndex()) {
                item?.let {
                    if (it.id.value == startItemId) {
                        lazyListState.requestScrollToItem(index)
                        isFound = true
                    }
                }
                if (isFound) break
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DefaultActionBar(
            onBackButtonClicked = { navController.popBackStack() },
            title = "Posts"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            if (postsLazyPagingItems.loadState.prepend is LoadState.Loading ||
                postsLazyPagingItems.loadState.refresh is LoadState.Loading
            ) {
                item { CircularProgressIndicatorRow() }
            }
            items(
                count = postsLazyPagingItems.itemCount,
                key = postsLazyPagingItems.itemKey { it.id },
                contentType = postsLazyPagingItems.itemContentType { it }
            ) { index ->
                postsLazyPagingItems[index]?.let {
                    PostItem(
                        postModel = mutatedStates[it.id.value] ?: it,
                        snackbarHost = snackbarHost,
                        navController = navController,
                        onStateMutation = {mutatedModel ->
                            mutatedStates[mutatedModel.id.value] = mutatedModel
                        }
                    )
                } ?: CircularProgressIndicatorRow()
            }
            if (postsLazyPagingItems.loadState.append is LoadState.Loading) {
                item { CircularProgressIndicatorRow() }
            }
        }
    }
}

@Serializable
data class PostsScreenRoute(
    val startItemIndex: Int,
    val startItemId: String,
    val authorId: String
)