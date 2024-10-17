package com.mobi.ripple.feature_app.feature_profile.presentation.profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.components.OptionItem
import com.mobi.ripple.core.presentation.components.PlusIcon
import com.mobi.ripple.core.presentation.profile.model.UserProfileInfoModel
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.theme.Shapes
import com.mobi.ripple.core.util.FormattableNumber
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun ProfileHeaderSection(
    userProfileInfoModel: UserProfileInfoModel,
    profilePicture: ImageBitmap?,
    onSettingsClicked: () -> Unit = {},
    onUploadPfpRequested: (Uri) -> Unit,
    onDeletePfpRequested: () -> Unit,
    onCreatePostRequested: (Uri) -> Unit,
    onFollowersClicked: () -> Unit,
    onFollowingClicked: () -> Unit
) {
    val pfpImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                onUploadPfpRequested.invoke(it)
            }
        }
    )
    val postImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                onCreatePostRequested(it)
            }
        }
    )

    val interactionSource = remember { MutableInteractionSource() }
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
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                SettingsIcon { onSettingsClicked() }
            }
            Column(
                modifier = Modifier.offset(y = (-14).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PfpImage(
                    image = profilePicture,
                    onUploadPfpRequested = {
                        pfpImagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    onDeletePfpRequested = onDeletePfpRequested
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
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onFollowersClicked()
                        }
                        .padding(start = 10.dp, bottom = 3.dp),
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
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onFollowingClicked()
                        }
                        .padding(end = 10.dp, bottom = 3.dp),
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
                Button(
                    onClick = {
                        postImagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    Modifier
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors()
                        .copy(
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = Shapes.small,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    PlusIcon(
                        Modifier
                            .padding(end = 5.dp)
                            .size(26.dp)
                    )
                    Text(
                        text = "Add post",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PfpImage(
    image: ImageBitmap?,
    onUploadPfpRequested: () -> Unit,
    onDeletePfpRequested: () -> Unit
) {
    val pfpOptionsSheetState = rememberModalBottomSheetState()
    val showPfpBottomSheet = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = showPfpBottomSheet.value) {
        coroutineScope {
            launch {
                if (showPfpBottomSheet.value) {
                    pfpOptionsSheetState.show()
                } else pfpOptionsSheetState.hide()
            }
        }
    }

    val imageModifier = Modifier
        .size(130.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.background)
        .border(
            width = 3.dp,
            color = MaterialTheme.colorScheme.onBackground,
            shape = CircleShape
        )
        .clickable { showPfpBottomSheet.value = true }
    if (image == null) {
        Image(
            modifier = imageModifier,
            painter = painterResource(id = R.drawable.user_profile_btn),
            contentDescription = "userImage"
        )
    } else {
        Image(
            modifier = imageModifier,
            bitmap = image,
            contentScale = ContentScale.Crop,
            contentDescription = "userImage"
        )
    }
    PfpBottomSheet(
        show = showPfpBottomSheet.value,
        sheetState = pfpOptionsSheetState,
        onDismissRequest = { showPfpBottomSheet.value = false },
        onUploadPfpClicked = {
            showPfpBottomSheet.value = false
            onUploadPfpRequested()
        },
        onDeletePfpClicked = {
            showPfpBottomSheet.value = false
            onDeletePfpRequested()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PfpBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onUploadPfpClicked: () -> Unit,
    onDeletePfpClicked: () -> Unit
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
                UploadNewPfpOption(onClick = onUploadPfpClicked)
                DeletePfpOption(onClick = onDeletePfpClicked)
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun UploadNewPfpOption(onClick: () -> Unit) {
    OptionItem(
        iconId = R.drawable.upload_image_icon,
        text = "Change profile picture",
        onClick = onClick
    )
}

@Composable
private fun DeletePfpOption(onClick: () -> Unit) {
    OptionItem(
        iconId = R.drawable.delete_icon,
        text = "Delete profile picture",
        onClick = onClick
    )
}

@Composable
private fun SettingsIcon(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Icon(
        modifier = modifier
            .padding(top = 12.dp, end = 12.dp)
            .size(26.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        painter = painterResource(id = R.drawable.setting_icon),
        tint = MaterialTheme.colorScheme.onSurface,
        contentDescription = "settings"
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderSectionPreview() {
    RippleTheme {
        ProfileHeaderSection(
            UserProfileInfoModel(
                id = "",
                fullName = "Melih John",
                userName = "meho35",
                email = "s@s.b",
                bio = "I am a professional photographer and content creator. " +
                        "I teach people how to make better photos.",
                followers = 127,
                following = 123200,
                isFollowed = true,
                isActive = false,
                lastActive = Instant.now(),
                postsCount = 0L,
            ),
            profilePicture = null,
            onUploadPfpRequested = {},
            onDeletePfpRequested = {},
            onCreatePostRequested = {},
            onFollowersClicked = {},
            onFollowingClicked = {}
        )
    }
}