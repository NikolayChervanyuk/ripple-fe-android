package com.mobi.ripple.feature_app.feature_feed.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.presentation.components.Logo
import com.mobi.ripple.core.presentation.components.RippleBadge
import com.mobi.ripple.core.presentation.components.SendIcon
import com.mobi.ripple.core.theme.Shapes
import com.mobi.ripple.core.theme.jostFamily
import com.mobi.ripple.feature_app.MessageManager
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.ChatsScreenRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTopBar(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    messageManager: MessageManager
) {
    val outlineColor = MaterialTheme.colorScheme.outline
    val coroutineScope = rememberCoroutineScope()

    val unreadMessageCount = rememberSaveable {
        mutableIntStateOf(0)
    }
    LaunchedEffect(key1 = true) {
        unreadMessageCount.intValue = messageManager.cacheQueries.getUnreadMessagesCount()
    }
    TopAppBar(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    color = outlineColor,
                    strokeWidth = 2.dp.toPx()
                )
            },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Logo(Modifier.size(36.dp))
                Text(
                    text = "Ripple",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 12.dp),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = jostFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        actions = {
            SendIcon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clip(Shapes.small)
                    .clickable {
                        coroutineScope.launch {
                            GlobalAppManager.isChatOpened.value = true
                            navController.navigate(ChatsScreenRoute)
                        }
                    }
                    .size(30.dp)
                    .padding(2.dp),
                badge = {
                    RippleBadge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 0.dp, y = (-5).dp)
                            .scale(1f),
                        counter = unreadMessageCount.intValue
                    )
                }
            )
        },
        scrollBehavior = scrollBehavior,
    )
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = true)
//@Composable
//private fun FeedTopBarPreview() {
//    RippleTheme {
//        FeedTopBar(
//            rememberNavController(),
//            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
//        )
//    }
//}