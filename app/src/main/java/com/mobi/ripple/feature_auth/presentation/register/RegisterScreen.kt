package com.mobi.ripple.feature_auth.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.DefaultSnackbar
import com.mobi.ripple.core.presentation.Logo
import com.mobi.ripple.core.presentation.PasswordTextInput
import com.mobi.ripple.core.presentation.UsernameTextField
import com.mobi.ripple.core.theme.PaddingMedium
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_auth.presentation.components.EmailTextField
import com.mobi.ripple.feature_auth.presentation.components.LargeButton
import com.mobi.ripple.feature_auth.presentation.login.LoginScreenRoute
import com.mobi.ripple.feature_auth.presentation.register.components.BackButton
import com.mobi.ripple.feature_auth.presentation.register.components.FullNameTextField
import com.mobi.ripple.feature_auth.presentation.register.components.HeaderText
import com.mobi.ripple.feature_auth.presentation.register.components.RegistrationErrorText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is RegisterViewModel.UiEvent.RouteBack -> {
                    navController.navigate(LoginScreenRoute) {
                        popUpTo(LoginScreenRoute) {
                            inclusive = true
                        }
                    }
                }

                is RegisterViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .consumeWindowInsets(WindowInsets.statusBars)
            .zIndex(1f)
    ) {
        BackButton(
            paddingValues = PaddingValues(start = 16.dp, top = 16.dp)
        ) {
            viewModel.onEvent(RegisterEvent.BackButtonPressed)
        }
    }
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .consumeWindowInsets(WindowInsets.statusBars)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            //LogoHeader(Modifier.padding(to), stringResource(R.string.join_us))
            Logo(
                modifier = Modifier
                    .padding(top = 48.dp)
                    .size(92.dp)
            )
            HeaderText("Registration")
            FullNameTextField(
                modifier = Modifier.padding(bottom = 32.dp),
                onTextChanged = { viewModel.onEvent(RegisterEvent.FullNameChanged(it)) },
                showInvalidMessage = state.value.showFullNameInvalidError.value
            )
            UsernameTextField(
                modifier = Modifier.padding(bottom = 32.dp),
                onTextChanged = {
                    viewModel.onEvent(RegisterEvent.UsernameChanged(it))
                },
                isUsernameTaken = state.value.isUsernameTaken.value,
                showUsernameTaken = state.value.showUsernameTaken.value
            )
            EmailTextField(
                modifier = Modifier.padding(bottom = 32.dp),
                onTextChanged = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
                showInvalidMessage = state.value.showEmailInvalidError.value,
                showEmailTakenMessage = state.value.isEmailTaken.value
            )
            PasswordTextInput(
                modifier = Modifier.padding(bottom = 32.dp),
                shouldShowPasswordRequirement = state.value.isPasswordInvalid.value,
                onTextChanged = { viewModel.onEvent(RegisterEvent.PasswordChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )
            PasswordTextInput(
                modifier = Modifier.padding(bottom = 8.dp),
                placeholder = stringResource(R.string.confirm_password),
                onTextChanged = { viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                showPasswordsNotMatching = state.value.showPasswordsNotMatchingError.value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Done
                )
            )

            RegistrationErrorText(
                show = state.value.showRegistrationError.value,
                message = state.value.registrationErrorMessage.value
            )
            LargeButton(
                modifier = Modifier.padding(top = 48.dp),
                text = stringResource(R.string.register),
                onClick = { viewModel.onEvent(RegisterEvent.RegisterButtonClicked) }
            )
            Spacer(modifier = Modifier.height(40.dp))
            DefaultSnackbar(hostState = snackbarHostState)
        }
    }
}

@Serializable
object RegisterScreenRoute : RouteType


@Preview(showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    RippleTheme {
        RegisterScreen(hiltViewModel<RegisterViewModel>(), rememberNavController())
    }
}