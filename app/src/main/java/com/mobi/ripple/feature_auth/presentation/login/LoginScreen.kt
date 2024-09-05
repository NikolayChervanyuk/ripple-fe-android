package com.mobi.ripple.feature_auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.components.DefaultSnackbar
import com.mobi.ripple.core.presentation.components.PasswordTextInput
import com.mobi.ripple.core.theme.PaddingMedium
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_auth.presentation.components.EmailTextField
import com.mobi.ripple.feature_auth.presentation.components.LargeButton
import com.mobi.ripple.feature_auth.presentation.components.LogoHeader
import com.mobi.ripple.feature_auth.presentation.login.components.ForgottenPasswordText
import com.mobi.ripple.feature_auth.presentation.login.components.NoAccountText
import com.mobi.ripple.feature_auth.presentation.register.RegisterScreenRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is LoginViewModel.UiEvent.NavigateToSignUp -> {
                    navController.navigate(RegisterScreenRoute)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .consumeWindowInsets(WindowInsets.statusBars)
            .padding(PaddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LogoHeader(stringResource(R.string.welcome_back))
        EmailTextField(
            modifier = Modifier.padding(bottom = 32.dp),
            onTextChanged = { viewModel.onEvent(LoginEvent.IdentifierTextChanged(it)) }
        )
        PasswordTextInput(
            modifier = Modifier.padding(bottom = 32.dp),
            onTextChanged = {
                viewModel.onEvent(LoginEvent.PasswordTextChanged(it))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        LargeButton(
            text = stringResource(R.string.login),
            onClick = { viewModel.onEvent(LoginEvent.Login) }
        )
        ForgottenPasswordText(onClick = { viewModel.onEvent(LoginEvent.PasswordChange) })
        NoAccountText(onClick = { viewModel.onEvent(LoginEvent.SignUp) })
    }
    DefaultSnackbar(hostState = snackbarHostState)
}

@Serializable
object LoginScreenRoute : RouteType
//@Serializable
//data class LoginScreenRoute(
//    val email: String?
//) : RouteType

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    RippleTheme {
        LoginScreen(viewModel<LoginViewModel>(), NavHostController(LocalContext.current))
    }
}