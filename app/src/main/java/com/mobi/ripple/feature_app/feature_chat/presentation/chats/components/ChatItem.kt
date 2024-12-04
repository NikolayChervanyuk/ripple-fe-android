package com.mobi.ripple.feature_app.feature_chat.presentation.chats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.components.PictureFrame
import com.mobi.ripple.core.theme.LikeRed
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.util.InstantPeriodTransformer
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.model.SimpleChatModel
import java.time.Instant

@Composable
fun ChatItem(
    simpleChat: SimpleChatModel,
    onChatClick: (chatId: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onChatClick(simpleChat.chatId)
            }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PictureFrame(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(42.dp),
            picture = simpleChat.chatPicture,
            placeHolderDrawableId = R.drawable.group_icon,
            innerPadding = PaddingValues(top = 5.dp),
            borderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(
            modifier = Modifier.weight(.93f)
        ) {
            GroupName(
                modifier = Modifier.padding(bottom = 4.dp),
                text = simpleChat.chatName,
                lastSentTime = simpleChat.lastSentTime
            ) //TODO
            LastMessage(
                text = simpleChat.lastChatMessage,
                isUnread = simpleChat.isUnread
            )

        }
    }
}

@Composable
private fun GroupName(
    modifier: Modifier = Modifier,
    text: String,
    lastSentTime: Instant
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(.9f),
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(.01f))
        Text(
            modifier = Modifier,
            text = InstantPeriodTransformer
                .transformToPassedTimeString(lastSentTime),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LastMessage(
    modifier: Modifier = Modifier,
    text: String,
    isUnread: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.weight(.85f),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(modifier = Modifier.weight(.02f))
        if (isUnread) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LikeRed)
                    .size(8.dp)

            )
        }
    }
}

@Preview
@Composable
private fun ChatItemPreview() {
    RippleTheme {
        Surface {
            ChatItem(SimpleChatModel(
                chatId = "",
                chatName = "<Group name>",
                chatPicture = null,
                lastChatMessage = "<Sample last message sent from Ivancho123 and company>",
                isUnread = false,
                lastSentTime = Instant.now().minusSeconds(2)
            ), {}
            )
        }
    }
}