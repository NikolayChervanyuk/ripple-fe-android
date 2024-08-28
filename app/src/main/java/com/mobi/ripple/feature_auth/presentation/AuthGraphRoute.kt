package com.mobi.ripple.feature_auth.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_auth.presentation.login.LoginScreen
import com.mobi.ripple.feature_auth.presentation.login.LoginScreenRoute
import com.mobi.ripple.feature_auth.presentation.login.LoginViewModel
import com.mobi.ripple.feature_auth.presentation.register.RegisterScreen
import com.mobi.ripple.feature_auth.presentation.register.RegisterScreenRoute
import com.mobi.ripple.feature_auth.presentation.register.RegisterViewModel
import kotlinx.serialization.Serializable

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AuthGraphRoute>(startDestination = LoginScreenRoute) {
        composable<LoginScreenRoute> {
            //viewmodel here
            val viewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(viewModel, navController) //should pass nav controller
        }
        composable<RegisterScreenRoute> {
            val viewModel: RegisterViewModel = hiltViewModel<RegisterViewModel>()
            RegisterScreen(viewModel,navController)
        }
//        composable<ForgottenPasswordScreenRoute> {
//        val viewModel = it.sharedViewModel<ForgottenPasswordViewModel>(navController = navController)
//            ForgottenPasswordScreen(navController)
//        }

    }

}

@Serializable
object AuthGraphRoute : RouteType