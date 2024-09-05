package com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mobi.ripple.core.presentation.components.DefaultDialog
import com.mobi.ripple.core.presentation.components.DefaultHeader
import com.mobi.ripple.core.presentation.components.DefaultSnackbar
import com.mobi.ripple.core.presentation.components.InvalidFieldMessage
import com.mobi.ripple.core.presentation.components.RippleInputField
import com.mobi.ripple.core.presentation.components.RippleMultilineInputField
import com.mobi.ripple.core.presentation.components.UsernameTextField
import com.mobi.ripple.core.presentation.components.WarningMessage
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.core.util.validator.FieldValidator
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileEvent
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileViewModel
import com.mobi.ripple.feature_auth.presentation.components.LargeButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun EditProfileScreen(
    viewModel: PersonalProfileViewModel,
    navController: NavHostController,
    sharedCoroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val userModelState =
        remember { derivedStateOf { state.value.userProfileInfoState.value.copy() } }
    
    var showRequiresLogoutMessage = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PersonalProfileViewModel.UiEvent.UiEditScreenEvent.EditProfileSuccessful -> {
                    sharedCoroutineScope.launch {
                        snackbarHostState.showSnackbar("Changes saved!")
                    }
                    navController.popBackStack(PersonalProfileScreenRoute, false)
                }

                else -> {}
            }
        }
    }
    DefaultDialog(
        header = {
            DefaultHeader(
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
                showRequiresLogout = showRequiresLogoutMessage.value,
                onTextChanged = {
                    showRequiresLogoutMessage.value = 
                        it != state.value.userProfileInfoState.value.userName
                    viewModel.onEvent(PersonalProfileEvent.EditScreenEvent.UsernameTextChanged(it))
                    userModelState.value.userName = it
                }
            )
            EmailField(
                textValue = userModelState.value.email ?: "",
                isEmailValid = FieldValidator.isEmailValid(userModelState.value.email ?: ""),
                isEmailTaken = state.value.editProfileState.value.isEmailTakenState.value,
                onTextChanged = {
                    viewModel.onEvent(PersonalProfileEvent.EditScreenEvent.EmailTextChanged(it))
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
                        PersonalProfileEvent.EditScreenEvent.EditProfileInfoRequested(userModelState.value)
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
    showRequiresLogout: Boolean,
    onTextChanged: (String) -> Unit
) {
    Column {
        WarningMessage(show = showRequiresLogout, message = "Changing username will log you out")
        EditProfileField(
            fieldTitle = "Username",
            textValue = textValue,
            onTextChanged = {},
            textFieldComposable = {
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
        textValue = textValue,
        onTextChanged = onTextChanged,
        bottomPadding = 32.dp
    ) {
        RippleMultilineInputField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            minLines = 5,
            text = textValue,
            onTextChanged = onTextChanged
        )
    }
}

@Composable
private fun EditProfileField(
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 14.dp,
    fieldTitle: String,
    keyboardOptions: KeyboardOptions? = null,
    readOnly: Boolean = false,
    textValue: String,
    onTextChanged: (String) -> Unit,
    textFieldComposable: @Composable (() -> Unit) = {
        RippleInputField(
            modifier = modifier.padding(bottom = 8.dp),
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
            Text(
                modifier = Modifier.padding(start = 15.dp, bottom = 2.dp),
                text = fieldTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        textFieldComposable()

    }
}

@Serializable
object EditProfileScreenRoute : RouteType