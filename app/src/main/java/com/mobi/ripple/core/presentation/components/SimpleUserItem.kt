package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mobi.ripple.R
import com.mobi.ripple.core.theme.BrightGreen
import com.mobi.ripple.core.theme.RippleTheme

data class SimpleUserItemModel(
    val id: String,
    val fullName: String?,
    val username: String,
    val isActive: Boolean,
    val profilePicture: ImageBitmap?
)

private typealias UserIdString = String
private typealias UsernameString = String
@Composable
fun SimpleUserItem(
    modifier: Modifier = Modifier,
    userModel: SimpleUserItemModel,
    onClick: (UserIdString, UsernameString) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(userModel.id, userModel.username) }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            profilePicture = userModel.profilePicture,
            isActive = userModel.isActive
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            val topText = userModel.fullName ?: userModel.username

            TopText(text = topText)
            userModel.fullName?.let {
                BottomText(text = userModel.username)
            }
        }
    }
}

@Composable
fun TopText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun BottomText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ProfilePicture(profilePicture: ImageBitmap?, isActive: Boolean) {
    val boxModifier = Modifier
        .padding(end = 10.dp)
        .size(38.dp)
    val imageModifier = Modifier
        .fillMaxSize()
        .zIndex(1f)
        .border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.onBackground,
            shape = CircleShape
        )
        .clip(CircleShape)
    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.BottomStart
    ) {
        ActivePin(show = isActive)
        profilePicture?.let {
            Image(
                modifier = imageModifier,
                bitmap = profilePicture,
                contentScale = ContentScale.Crop,
                contentDescription = "user profile picture"
            )
        } ?: Image(
            modifier = imageModifier.background(color = Color.White),
            painter = painterResource(id = R.drawable.user_profile_btn),
            contentDescription = "user profile picture"
        )
    }
}

@Composable
private fun ActivePin(show: Boolean) {
    if (show) {
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape

                )
                .size(11.dp)
                .padding(1.dp)
                .clip(CircleShape)

                .background(BrightGreen)
                .zIndex(Float.MAX_VALUE)
        )
    }
}

@Preview
@Composable
private fun SimpleUserItemModelPreview() {
    RippleTheme {
        Surface {
            SimpleUserItem(
                userModel = SimpleUserItemModel(
                    id = "1234",
                    fullName = "Ivan Ivanov",
                    username = "ivan777",
                    isActive = true,
                    profilePicture = null
                ),
                onClick = {_, _ -> }
            )
        }
    }
}