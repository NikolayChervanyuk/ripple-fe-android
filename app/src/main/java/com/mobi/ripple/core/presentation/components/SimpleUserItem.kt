package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(38.dp),
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
private fun TopText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun BottomText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
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
                onClick = { _, _ -> }
            )
        }
    }
}