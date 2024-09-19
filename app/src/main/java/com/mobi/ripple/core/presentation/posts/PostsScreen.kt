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
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.presentation.components.CircularProgressIndicatorRow
import com.mobi.ripple.core.presentation.components.DefaultHeader
import com.mobi.ripple.core.presentation.post.PostItem
import com.mobi.ripple.core.presentation.profile.ProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileScreenRoute
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
        .postsFlowState
        .collectAsLazyPagingItems()
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

        if (postsLazyPagingItems.loadState.prepend is LoadState.Loading ||
            postsLazyPagingItems.loadState.refresh is LoadState.Loading
        ) {
            CircularProgressIndicatorRow()
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = postsLazyPagingItems.itemCount,
                key = postsLazyPagingItems.itemKey { it.postModel.id },
                contentType = postsLazyPagingItems.itemContentType { it }
            ) { index ->
                val item = postsLazyPagingItems[index]
                item?.let {
                    PostItem(
                        postItemModel = it,
                        snackbarHost = snackbarHost,
                        onProfileNavigationRequest = { requestedUsername ->
                            GlobalAppManager.storedUsername?.let { storedUsername ->
                                if (storedUsername == requestedUsername) {
                                    navController.navigate(PersonalProfileScreenRoute)
                                } else navController.navigate(ProfileScreenRoute(requestedUsername))
                            }
                        }
                    )
                }
            }
        }
        if (postsLazyPagingItems.loadState.append is LoadState.Loading) {
            CircularProgressIndicatorRow()
        }
    }
}

@Serializable
data class PostsScreenRoute(
    val startIndex: Int,
    val authorId: String
    //TODO
)
