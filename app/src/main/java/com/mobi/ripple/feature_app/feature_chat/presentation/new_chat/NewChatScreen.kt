package com.mobi.ripple.feature_app.feature_chat.presentation.new_chat

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.core.presentation.components.DefaultActionBar
import com.mobi.ripple.core.presentation.components.DefaultCircularProgressIndicator
import com.mobi.ripple.core.presentation.components.DefaultSearchField
import com.mobi.ripple.core.presentation.components.DefaultSnackbar
import com.mobi.ripple.core.presentation.components.NewChatIcon
import com.mobi.ripple.core.presentation.components.PictureFrame
import com.mobi.ripple.core.presentation.components.SimpleUserItem
import com.mobi.ripple.core.presentation.components.SimpleUserItemModel
import com.mobi.ripple.core.theme.Shapes
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.ChatScreenRoute
import com.mobi.ripple.feature_app.feature_chat.presentation.model.SimpleChatUserModel
import com.mobi.ripple.feature_app.feature_chat.presentation.model.asSimpleChatUserModel
import com.mobi.ripple.feature_app.feature_chat.presentation.new_chat.components.ChatNameField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun NewChatScreen(
    initialUserId: String?,
    viewModel: NewChatViewModel,
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    sharedCoroutineScope: CoroutineScope
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val addedUsers = rememberSaveable {
        mutableIntStateOf(0)
    }

//    val addedParticipantsList = remember { mutableStateListOf<SimpleChatUserModel>() }
    val foundUsersList: MutableState<List<SimpleUserItemModel>> = remember {
        state.foundUsersList
    }
    LaunchedEffect(key1 = state.foundUsersList.value) {
        foundUsersList.value = state.foundUsersList.value
    }

    LaunchedEffect(key1 = true) {
        initialUserId?.let {
            sharedCoroutineScope.launch {
                viewModel.getInitialUser(it)?.let { user ->
                    state.addedParticipantsList.add(user)
                } ?: snackBarState.showSnackbar("Network error")
            }
        }
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NewChatViewModel.UiEvent.ChatCreatedSuccessfully -> {
                    navController.navigate(ChatScreenRoute(event.chatId)) {
                        popUpTo<NewChatScreenRoute> { inclusive = true }
                    }
                }
            }
        }
    }

    val participantsListState = rememberLazyListState()
    val isOperationInProgress = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }

    NewChatScaffold(
        navController = navController,
        snackBarState = snackBarState,
        participantsCount = state.addedParticipantsList.size,
        onCreateNewChat = {
            if (state.addedParticipantsList.size > 0) {
                isOperationInProgress.value = true
                viewModel.onEvent(NewChatEvent.CreateNewChat(state.addedParticipantsList.toList()))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) {
            ChatNameField(
                visible = addedUsers.intValue > 1,
                onTextChanged = {
                    viewModel.onEvent(NewChatEvent.ChatNameTextChanged(it))
                })
            AnimatedVisibility(
                visible = addedUsers.intValue > 0,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(200)
                ),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(200)
                )
            ) {

            LazyRow(
                state = participantsListState,
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
            ) {
                items(
                    count = state.addedParticipantsList.size,
                    key = { index -> state.addedParticipantsList[index].userId },
                    contentType = { index -> state.addedParticipantsList[index] }
                ) { index ->
                    NewParticipantItem(
                        index = index,
                        simpleChatUser = state.addedParticipantsList[index],
                        onClick = {
                            state.addedParticipantsList.removeAt(index)
                            --addedUsers.intValue
                        }
                    )
                }
            }
        }
            DefaultSearchField(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                onTextChanged = { newText ->
                    searchText.value = newText
                    viewModel.onEvent(NewChatEvent.SearchTextChanged(newText))
                },
                placeholder = "Search users",
                text = searchText.value
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = foundUsersList.value.size,
                    key = { index -> foundUsersList.value[index].id },
                    contentType = { index -> foundUsersList.value[index] }
                ) { index ->
                    SimpleUserItem(
                        userModel = foundUsersList.value[index],
                        onClick = { userId, _ ->
                            state.addedParticipantsList.add(
                                foundUsersList.value[index].asSimpleChatUserModel()
                            )
                            ++addedUsers.intValue
                            val mutableList = state.foundUsersList.value.toMutableList()
                            mutableList.removeIf { it.id == userId }
                            state.foundUsersList.value = mutableList
                        }
                    )
                }
            }
        }
    }
    if (isOperationInProgress.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = .5f))
                .zIndex(Float.MAX_VALUE),
            contentAlignment = Alignment.Center
        ) {
            DefaultCircularProgressIndicator()
        }
    }
}

@Composable
private fun NewChatScaffold(
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    participantsCount: Int,
    onCreateNewChat: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val backClicked = remember { mutableStateOf(false) }
    BackHandler {
        if (!backClicked.value) {
            backClicked.value = true
            navController.popBackStack()
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        snackbarHost = { DefaultSnackbar(hostState = snackBarState) },
        floatingActionButton = {
            CreateGroupButton(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.onBackground
                    ),
                show = participantsCount > 0,
                onClick = { onCreateNewChat() }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            DefaultActionBar(
                onBackButtonClicked = {
                    if (!backClicked.value) {
                        backClicked.value = true
                        navController.popBackStack()
                    }
                },
                title = "New chat",
//                actionComposable = {
//                    CancelButton(modifier = Modifier
//                        .clip(Shapes.small)
//                        .clickable {
//                            if (!backClicked.value) {
//                                backClicked.value = true
//                                navController.popBackStack()
//                            }
//                        }
//                        .size(20.dp)
//                    )
//                }
            )
        }
    ) { paddingValues ->
        content.invoke(paddingValues)
    }
}

@Composable
fun NewParticipantItem(
    index: Int,
    simpleChatUser: SimpleChatUserModel,
    onClick: (index: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick(index) }
            .padding(horizontal = 8.dp)
            .width(42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PictureFrame(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .size(40.dp),
            picture = simpleChatUser.userPfp,
            isActive = simpleChatUser.isActive
        )
        Text(
            text = simpleChatUser.fullName ?: simpleChatUser.username,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Composable
private fun CreateGroupButton(
    modifier: Modifier = Modifier,
    show: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = show,
        enter = slideInHorizontally(animationSpec = tween(150)) { it },
        exit = slideOutHorizontally(animationSpec = tween(150)) { it }
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            NewChatIcon(
                modifier = Modifier
                    .clip(Shapes.small)
                    .clickable { onClick() }
                    .padding(10.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Serializable
data class NewChatScreenRoute(val initialUserId: String? = null)