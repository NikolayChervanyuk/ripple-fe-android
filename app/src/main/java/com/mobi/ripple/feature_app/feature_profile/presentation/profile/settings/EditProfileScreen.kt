package com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.core.presentation.DefaultDialog
import com.mobi.ripple.core.presentation.DefaultDialogHeader
import com.mobi.ripple.core.presentation.DefaultSnackbar
import com.mobi.ripple.core.presentation.InvalidFieldMessage
import com.mobi.ripple.core.presentation.RippleInputField
import com.mobi.ripple.core.presentation.UsernameTextField
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.core.util.validator.FieldValidator
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileEvent
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileViewModel
import com.mobi.ripple.feature_auth.presentation.components.LargeButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavHostController,
    sharedCoroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val userModelState =
        remember { derivedStateOf { state.value.userProfileInfoState.value.copy() } }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ProfileViewModel.UiEvent.UiEditScreenEvent.EditProfileSuccessful -> {
                    sharedCoroutineScope.launch {
                        snackbarHostState.showSnackbar("Changes saved!")
                    }
                    navController.popBackStack(ProfileScreenRoute, false)
                }

                else -> {}
            }
        }
    }
    DefaultDialog(
        header = {
            DefaultDialogHeader(
                onBackButtonClicked = {
                    navController.popBackStack(SettingsScreenRoute, false)
                },
                title = "Edit profile"
            )
        },
        onDismissRequest = {
            navController.popBackStack(SettingsScreenRoute, false)
        }
    ) {
        DefaultSnackbar(hostState = snackbarHostState)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FullNameField(
                textValue = userModelState.value.fullName ?: "",
                onTextChanged = { userModelState.value.fullName = it }
            )
            UsernameField(
                textValue = userModelState.value.userName,
                isUsernameTaken = state.value.editProfileState.value.isUsernameTakenState.value,
                onTextChanged = {
                    viewModel.onEvent(ProfileEvent.EditScreenEvent.UsernameTextChanged(it))
                    userModelState.value.userName = it
                }
            )
            EmailField(
                textValue = userModelState.value.email ?: "",
                isEmailValid = FieldValidator.isEmailValid(userModelState.value.email ?: ""),
                isEmailTaken = state.value.editProfileState.value.isEmailTakenState.value,
                onTextChanged = {
                    viewModel.onEvent(ProfileEvent.EditScreenEvent.EmailTextChanged(it))
                    userModelState.value.email = it
                }
            )
            BioField(
                textValue = userModelState.value.bio ?: "",
                onTextChanged = { userModelState.value.bio = it }
            )
            LargeButton(
                text = "Save",
                onClick = {
                    viewModel.onEvent(
                        ProfileEvent.EditScreenEvent.EditProfileInfoRequested(userModelState.value)
                    )
                }
            )
        }
    }
}

@Composable
private fun FullNameField(textValue: String, onTextChanged: (String) -> Unit) {
    EditProfileField(
        fieldTitle = "Full name",
        textValue = textValue,
        onTextChanged = onTextChanged
    )
}

@Composable
private fun UsernameField(
    textValue: String,
    isUsernameTaken: Boolean,
    onTextChanged: (String) -> Unit
) {
    EditProfileField(
        fieldTitle = "Username",
        textValue = textValue,
        onTextChanged = {},
        texFieldComposable = {
            UsernameTextField(
                onTextChanged = onTextChanged,
                isUsernameTaken = isUsernameTaken,
                showUsernameTaken = isUsernameTaken,
                text = textValue,
                placeholder = "",
                leadingIcon = null
            )
        }
    )
}

@Composable
private fun EmailField(
    textValue: String,
    isEmailValid: Boolean,
    isEmailTaken: Boolean,
    onTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        EditProfileField(
            fieldTitle = "Email",
            readOnly = true,
            bottomPadding = if (!isEmailValid || isEmailTaken) 0.dp else 14.dp,
            textValue = textValue,
            onTextChanged = onTextChanged
        )
        InvalidFieldMessage(
            modifier = Modifier.padding(bottom = 8.dp),
            show = !isEmailValid,
            message = "Invalid email"
        )
        InvalidFieldMessage(
            show = isEmailValid && isEmailTaken,
            message = "$textValue is already taken"
        )
    }
}

@Composable
private fun BioField(textValue: String, onTextChanged: (String) -> Unit) {
    EditProfileField(

        fieldTitle = "Bio",
        maxLines = 30,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done
        ),
        textValue = textValue,
        onTextChanged = onTextChanged
    )
}

@Composable
private fun EditProfileField(
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 14.dp,
    fieldTitle: String,
    keyboardOptions: KeyboardOptions? = null,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    textValue: String,
    onTextChanged: (String) -> Unit,
    texFieldComposable: @Composable (() -> Unit) = {
        RippleInputField(
            modifier = modifier.padding(bottom = 8.dp),
            maxLines = maxLines,
            keyboardOptions = keyboardOptions ?: KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            ),
            text = textValue,
            readOnly = readOnly,
            onTextChanged = { onTextChanged(it) }
        )
    }

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = bottomPadding)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 2.dp)
        ) {
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Text(
                text = fieldTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        texFieldComposable()

    }
}

@Serializable
object EditProfileScreenRoute : RouteType