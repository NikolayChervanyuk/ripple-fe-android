package com.mobi.ripple.feature_app.feature_chat.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.mobi.ripple.feature_app.MessageManager
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.ChatScreen
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.ChatScreenRoute
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.ChatViewModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.ChatsScreen
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.ChatsScreenRoute
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.ChatsViewModel
import com.mobi.ripple.feature_app.feature_chat.presentation.new_chat.NewChatScreen
import com.mobi.ripple.feature_app.feature_chat.presentation.new_chat.NewChatScreenRoute
import com.mobi.ripple.feature_app.feature_chat.presentation.new_chat.NewChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

fun NavGraphBuilder.chatsGraph(
    mainNavController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedCoroutineScope: CoroutineScope,
    messageManager: MessageManager,
    userId: String? = null
) {
    navigation<ChatsGraphRoute>(
        startDestination = if (userId == null) ChatsScreenRoute
        else ChatScreenRoute(userId)
    ) {
        composable<ChatsScreenRoute> {
            val viewModel =
                hiltViewModel<ChatsViewModel, ChatsViewModel.ChatsViewModelFactory> { factory ->
                    factory.create(messageManager)
                }
            ChatsScreen(
                viewModel = viewModel,
                navController = mainNavController,
                snackBarState = snackbarHostState,
                sharedCoroutineScope = sharedCoroutineScope
            )
        }
        composable<ChatScreenRoute> {
            val route = it.toRoute<ChatScreenRoute>()
            //TODO: Find chat id from userId, if any
            val viewModel =
                hiltViewModel<ChatViewModel, ChatViewModel.ChatViewModelFactory> { factory ->
                    factory.create(route.chatId, messageManager)
                }
            ChatScreen(
                chatId = route.chatId,
                viewModel = viewModel,
                navController = mainNavController,
                snackBarState = snackbarHostState,
                sharedCoroutineScope = sharedCoroutineScope
            )
        }
        composable<NewChatScreenRoute> {
            val route = it.toRoute<NewChatScreenRoute>()
            val viewModel =
                hiltViewModel<NewChatViewModel, NewChatViewModel.NewChatViewModelFactory> { factory ->
                    factory.create(messageManager)
                }
            NewChatScreen(
                initialUserId = route.initialUserId,
                viewModel = viewModel,
                navController = mainNavController,
                snackBarState = snackbarHostState,
                sharedCoroutineScope = sharedCoroutineScope
            )
        }
    }
}

@Serializable
object ChatsGraphRoute