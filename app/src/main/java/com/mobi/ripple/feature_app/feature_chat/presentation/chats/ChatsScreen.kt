package com.mobi.ripple.feature_app.feature_chat.presentation.chats

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.presentation.components.CircularProgressIndicatorRow
import com.mobi.ripple.core.presentation.components.DefaultActionBar
import com.mobi.ripple.core.presentation.components.DefaultSearchField
import com.mobi.ripple.core.presentation.components.DefaultSnackbar
import com.mobi.ripple.core.presentation.components.NewChatIcon
import com.mobi.ripple.core.theme.Shapes
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.ChatScreenRoute
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.components.ChatItem
import com.mobi.ripple.feature_app.feature_chat.presentation.new_chat.NewChatScreenRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel,
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    sharedCoroutineScope: CoroutineScope
) {

    val lazyListState = rememberLazyListState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lazyPagingChats = state.chatsFlow.collectAsLazyPagingItems()
    val searchText = remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        viewModel.messageManager.receivedMessagesFlow.collectLatest {
            lazyPagingChats.refresh()
        }
    }

    ChatsScaffold(
        navController = navController,
        snackBarState = snackBarState,
        onNewChatRequested = { navController.navigate(NewChatScreenRoute()) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                DefaultSearchField(
                    modifier = Modifier
                        .padding(vertical = 6.dp),
                    onTextChanged = { newText ->
                        searchText.value = newText
                        viewModel.onEvent(ChatsEvent.SearchChatsTextChanged(newText))
                    },
                    placeholder = "Search chats",
                    text = searchText.value
                )
            }
            if (lazyPagingChats.itemCount == 0) {
                item { NoChatsMessage() }
            } else {
                items(
                    count = lazyPagingChats.itemCount,
                    key = lazyPagingChats.itemKey { it.chatId },
                    contentType = lazyPagingChats.itemContentType { it }
                ) { index ->
                    lazyPagingChats[index]?.let {
                        ChatItem(
                            simpleChat = it,
                            onChatClick = { chatId ->
                                navController.navigate(ChatScreenRoute(chatId))
                            }
                        )
                    }
                }
            }
            if (lazyPagingChats.loadState.append is LoadState.Loading) {
                item { CircularProgressIndicatorRow() }
            }
        }
    }
}


@Composable
private fun ChatsScaffold(
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    onNewChatRequested: (initialUserId: String?) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val backClicked = remember { mutableStateOf(false) }
    BackHandler {
        if (!backClicked.value) {
            GlobalAppManager.isChatOpened.value = false
            backClicked.value = true
            navController.popBackStack()
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        snackbarHost = { DefaultSnackbar(hostState = snackBarState) },
        topBar = {
            DefaultActionBar(
                onBackButtonClicked = {
                    if (!backClicked.value) {
                        GlobalAppManager.isChatOpened.value = false
                        backClicked.value = true
                        navController.popBackStack()
                    }
                },
                title = "Chats",
                actionComposable = {
                    NewChatIcon(
                        modifier = Modifier
                            .clip(Shapes.small)
                            .clickable { onNewChatRequested(null) }
                            .size(28.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        content.invoke(paddingValues)
    }
}

@Composable
private fun NoChatsMessage() {
    Column(
        modifier = Modifier.padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 6.dp),
            text = "No chats yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Row {
            Text(
                text = "Create one by clicking",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            NewChatIcon(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(Shapes.small)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}

@Serializable
object ChatsScreenRoute

//@Preview
//@Composable
//private fun ChatsScreenPreview() {
//    RippleTheme {
//        Surface {
//            ChatsScreen(
//                ChatsViewModel(),
//                rememberNavController(),
//                SnackbarHostState(),
//                rememberCoroutineScope()
//            )
//        }
//    }
//}