package com.mobi.ripple.core.presentation.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.components.BackArrowIcon
import com.mobi.ripple.core.presentation.components.OptionItem
import com.mobi.ripple.core.presentation.components.PlusIcon
import com.mobi.ripple.core.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileActionsRow(
    isFollowed: Boolean,
    onFollowStatusChangeRequest: () -> Unit,
    onMessageRequest: () -> Unit
) {

    val followedSheetState = rememberModalBottomSheetState()
    val showFollowedBottomSheet = rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, top = 25.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        Row(modifier = Modifier
            .clickable {
                if (isFollowed) {
                    showFollowedBottomSheet.value = true
                } else {
                    onFollowStatusChangeRequest()
                }
            }
            .clip(Shapes.small)
            .background(MaterialTheme.colorScheme.onSurfaceVariant)
            .weight(0.5f)
            .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (!isFollowed) {
                Text(
                    color = MaterialTheme.colorScheme.surface,
                    text = "Follow",
                    style = MaterialTheme.typography.titleSmall
                )
            } else {
                Text(
                    color = MaterialTheme.colorScheme.surface,
                    text = "Following",
                    style = MaterialTheme.typography.titleSmall
                )
                BackArrowIcon(
                    modifier = Modifier
                        .padding(start = 4.dp, top = 3.dp)
                        .rotate(-90f)
                        .size(12.dp),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Row(modifier = Modifier
            .clickable { onMessageRequest() }
            .clip(Shapes.small)
            .background(MaterialTheme.colorScheme.onSurfaceVariant)
            .weight(0.5f)
            .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                color = MaterialTheme.colorScheme.surface,
                text = "Message",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }

    FollowedBottomSheet(
        show = showFollowedBottomSheet.value,
        sheetState = followedSheetState,
        onDismissRequest = { showFollowedBottomSheet.value = false },
        onUnfollowClicked = {
            onFollowStatusChangeRequest()
            showFollowedBottomSheet.value = false
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FollowedBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onUnfollowClicked: () -> Unit
) {
    AnimatedVisibility(
        visible = show,
        enter = expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(200)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Bottom,
            animationSpec = tween(200)
        )
    ) {
        ModalBottomSheet(
            modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
            onDismissRequest = { onDismissRequest() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
               UnfollowOption(onClick = {
                   onUnfollowClicked()
               })
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}

@Composable
private fun UnfollowOption(onClick: () -> Unit) {
    OptionItem(
        iconId = R.drawable.user_remove_icon,
        text = "Unfollow",
        onClick = {onClick()})
}