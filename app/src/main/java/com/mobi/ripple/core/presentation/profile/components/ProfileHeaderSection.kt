package com.mobi.ripple.core.presentation.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.profile.model.UserProfileInfoModel
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.core.util.FormattableNumber

@Composable
fun ProfileHeaderSection(
    userProfileInfoModel: UserProfileInfoModel,
    profilePicture: ByteArray?,
    onPfpClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 38.dp)
                    .offset(y = (-14).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PfpImage(
                    image = profilePicture,
                    onClicked =  onPfpClicked
                )
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = userProfileInfoModel.fullName ?: "",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .padding(horizontal = 25.dp, vertical = 3.dp),
                    text = "@" + userProfileInfoModel.userName,
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {

            userProfileInfoModel.bio?.let {
                Text(
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp),
                    text = userProfileInfoModel.bio!!,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                )
            }
            val outlineColor = MaterialTheme.colorScheme.outline
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = outlineColor,
                            strokeWidth = 1.dp.toPx(),
                            start = Offset(x = 0f, y = size.height),
                            end = Offset(x = size.width, y = size.height)
                        )
                    }
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp, bottom = 3.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.titleSmall.toSpanStyle()
                        ) {
                            append(FormattableNumber(userProfileInfoModel.followers).format())
                        }
                        append(" ")
                        append("Followers")
                    }
                )
                Spacer(
                    Modifier
                        .padding(bottom = 8.dp)
                        .size(width = 1.dp, height = 26.dp)
                        .background(MaterialTheme.colorScheme.outline)
                )
                Text(
                    modifier = Modifier.padding(end = 10.dp, bottom = 3.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.titleSmall.toSpanStyle()
                        ) {
                            append(
                                FormattableNumber.format(
                                    userProfileInfoModel.following,
                                    shouldTrimOnZero = true
                                ).format()
                            )
                        }
                        append(" ")
                        append("Following")
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userProfileInfoModel.postsCount.toString() + " " + "Posts",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }
        }
    }
}

@Composable
private fun PfpImage(image: ByteArray?, onClicked: () -> Unit) {
    val imageModifier = Modifier
        .size(130.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.background)
        .border(
            width = 3.dp,
            color = MaterialTheme.colorScheme.onBackground,
            shape = CircleShape
        )
        .clickable { onClicked() }
    if (image == null) {
        Image(
            modifier = imageModifier,
            painter = painterResource(id = R.drawable.user_profile_btn),
            contentDescription = "userImage"
        )
    } else {
        Image(
            modifier = imageModifier,
            bitmap = BitmapUtils.convertImageByteArrayToBitmap(image).asImageBitmap(),
            contentScale = ContentScale.Crop,
            contentDescription = "userImage"
        )
    }
}