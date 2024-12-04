package com.mobi.ripple.feature_auth.domain.use_case

data class AuthUseCases(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val isUsernameTakenUseCase: IsUsernameTakenUseCase,
    val isEmailTakenUseCase: IsEmailTakenUseCase,
    val getSimpleAuthUserUseCase: GetSimpleAuthUserUseCase
)