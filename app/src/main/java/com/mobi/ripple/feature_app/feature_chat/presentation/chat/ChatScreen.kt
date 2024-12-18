package com.mobi.ripple.feature_app.feature_chat.presentation.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.mobi.ripple.core.presentation.components.ActionButton
import com.mobi.ripple.core.presentation.components.CircularProgressIndicatorRow
import com.mobi.ripple.core.presentation.components.DefaultActionBar
import com.mobi.ripple.core.presentation.components.DefaultCircularProgressIndicator
import com.mobi.ripple.core.presentation.components.DefaultSnackbar
import com.mobi.ripple.core.presentation.components.PictureFrame
import com.mobi.ripple.core.presentation.components.ReloadButton
import com.mobi.ripple.core.presentation.components.RippleMultilineInputField
import com.mobi.ripple.core.presentation.components.SendIcon
import com.mobi.ripple.core.theme.Shapes
import com.mobi.ripple.core.util.InstantPeriodTransformer
import com.mobi.ripple.core.util.SoundEffects
import com.mobi.ripple.core.util.validator.FieldValidator
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.ChatModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.MessageDataModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.MessageModel
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageResolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun ChatScreen(
    chatId: String,
    viewModel: ChatViewModel,
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    sharedCoroutineScope: CoroutineScope
) {

    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    val lazyColumnState = rememberLazyListState()
    val messageLazyItems = state.value
        .messagesFlow.collectAsLazyPagingItems()

//    LaunchedEffect(key1 = messageLazyItems.loadState.refresh) {
//        if(messageLazyItems.loadState.refresh is LoadState.NotLoading) {
//            lazyColumnState.requestScrollToItem(0)
//        }
//    }

    LaunchedEffect(key1 = true) {
        viewModel.markChatMessagesAsRead()
        viewModel.messageManager.receivedMessagesFlow.collectLatest {
            if (MessageResolver.getChatId(it) == chatId) {
                messageLazyItems.refresh()
                lazyColumnState.animateScrollToItem(0)
            }
        }
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ChatViewModel.UiEvent.LoadError -> {
                    snackBarState.showSnackbar(event.errorMessage)
                }

                is ChatViewModel.UiEvent.MessageCached, ChatViewModel.UiEvent.MessageSent -> {
                    messageLazyItems.refresh()
                    lazyColumnState.animateScrollToItem(0)
                }
            }
        }
    }

    ChatScaffold(
        navController = navController,
        snackBarState = snackBarState,
        chatModel = state.value.chatModel.value,
        //TODO
        onOptionsClicked = { sharedCoroutineScope.launch { snackBarState.showSnackbar("Not available") } }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .weight(.92f),
                reverseLayout = true,
                verticalArrangement = Arrangement.Top
            ) {
                if (messageLazyItems.loadState.append is LoadState.Loading) {
                    item { CircularProgressIndicatorRow() }
                }
                items(
                    count = messageLazyItems.itemCount,
                    key = messageLazyItems.itemKey { it.messageId },
                    contentType = messageLazyItems.itemContentType { it }
                ) { index ->
                    messageLazyItems[index]?.let { message ->
                        MessageItem(messageModel = message, onResendMessage = {
                            viewModel.onEvent(ChatEvent.ResendNewMessage(message))
                        })
                    } ?: DefaultCircularProgressIndicator()
                }
            }
            SendMessageRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)
                    .padding(6.dp),
                onSendMessage = { messageData ->
                    SoundEffects.NewMessage.play(context)
                    viewModel.onEvent(ChatEvent.SendNewMessageRequested(messageData))
                    messageLazyItems.refresh()
                    sharedCoroutineScope.launch {
                        lazyColumnState.requestScrollToItem(0)
                    }
                }
            )
        }
    }
}

@Composable
private fun MessageItem(
    messageModel: MessageModel,
    onResendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = if (messageModel.eventType == ChatEventType.NEW_MESSAGE) {
            if (messageModel.isMine) Arrangement.End
            else Arrangement.Start
        } else Arrangement.Center
    ) {
        val isResending = remember { mutableStateOf(false) }
        when (messageModel.eventType) {
            ChatEventType.NEW_MESSAGE -> {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!messageModel.isMine) {
                        PictureFrame(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .size(32.dp),
                            borderWidthDp = 1.dp,
                            picture = messageModel.authorPfp,
                            isActive = messageModel.isAuthorActive
                        )
                    }
                    Column(
                        modifier = Modifier
                            .clip(Shapes.medium)
                            .background(
                                color = if (messageModel.isMine) {
                                    MaterialTheme.colorScheme.outlineVariant
                                } else MaterialTheme.colorScheme.background
                            )
                            .padding(horizontal = 10.dp)
                            .padding(top = 10.dp, bottom = 2.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        if (!messageModel.isMine) {
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 1.dp)
                                    .width(IntrinsicSize.Max),
                                text = messageModel.authorUsername!!,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                textAlign = TextAlign.Start
                            )
                        }
                        Text(
                            modifier = Modifier.background(color = Color.Transparent),
                            text = messageModel.defaultTextMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (messageModel.isMine) {
                                MaterialTheme.colorScheme.background
                            } else MaterialTheme.colorScheme.onBackground
                        )
                        if (messageModel.isMine && !messageModel.isSent) {
                            Column(
                                modifier = Modifier
                                    .padding(3.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ReloadButton(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .padding(4.dp)
                                        .size(24.dp),
                                    tint = MaterialTheme.colorScheme.background,
                                    onClick = {
                                        isResending.value = true
                                        onResendMessage()
                                    }
                                )
                                Text(
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .background(color = Color.Transparent)
                                        .padding(top = 4.dp),
                                    text = if (isResending.value) "Sending..."
                                        else "Sending message failed",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }
                        }
                        Text(
                            modifier = Modifier.background(color = Color.Transparent),
                            text = InstantPeriodTransformer.transformToPassedTimeString(messageModel.sentDate),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (messageModel.isMine) {
                                MaterialTheme.colorScheme.background
                            } else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            ChatEventType.NEW_PARTICIPANT,
            ChatEventType.PARTICIPANT_LEFT,
            ChatEventType.PARTICIPANT_REMOVED -> {
                Text(
                    modifier = Modifier.background(color = Color.Transparent),
                    text = messageModel.defaultTextMessage,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            ChatEventType.CHAT_OPENED -> {} //TODO
            ChatEventType.CHAT_CREATED -> {
                Text(
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .padding(bottom = 30.dp, top = 16.dp),
                    text = messageModel.defaultTextMessage,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        if (!messageModel.isMine) Spacer(modifier = Modifier.width(35.dp))
    }

}

@Composable
private fun SendMessageRow(
    modifier: Modifier,
    onSendMessage: (MessageDataModel) -> Unit
) {
    val newText = rememberSaveable {
        mutableStateOf("")
    }
    val sendButtonTint = if (FieldValidator.isChatMessageValid(newText.value)) {
        MaterialTheme.colorScheme.onBackground
    } else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier.weight(.9f)
        ) {
            RippleMultilineInputField(
                placeholder = "Message...",
                onTextChanged = { newText.value = it },
                text = newText.value,
                minLines = 1,
                maxLines = 4
            )
        }
        SendIcon(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(28.dp)
                .clickable {
                    if (FieldValidator.isChatMessageValid(newText.value)) {
                        val msg = newText.value
                        onSendMessage(MessageDataModel(msg))
                        newText.value = ""
                    }
                }
                .size(18.dp),
            tint = sendButtonTint
        )
    }
}

@Composable
private fun ChatScaffold(
    navController: NavHostController,
    snackBarState: SnackbarHostState,
    chatModel: ChatModel,
    onOptionsClicked: () -> Unit,
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
        topBar = {
            DefaultActionBar(
                onBackButtonClicked = {
                    if (!backClicked.value) {
                        backClicked.value = true
                        navController.popBackStack()
                    }
                },
                title = chatModel.chatName,
                actionComposable = {
                    ActionButton(
                        modifier = Modifier
                            .clip(Shapes.small)
                            .size(20.dp),
                        onClick = onOptionsClicked
                    )
                }
            )
        }
    ) { paddingValues ->
        content.invoke(paddingValues)
    }

}

@Serializable
data class ChatScreenRoute(
    val chatId: String
)