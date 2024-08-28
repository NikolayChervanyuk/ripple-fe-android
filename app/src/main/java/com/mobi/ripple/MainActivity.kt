package com.mobi.ripple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobi.ripple.core.config.BuildConfig
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_app.AppScreen
import com.mobi.ripple.feature_app.AppScreenRoute
import com.mobi.ripple.feature_auth.presentation.AuthGraphRoute
import com.mobi.ripple.feature_auth.presentation.authGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootAppManager: RootAppManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setContent {
            RippleTheme {
                GlobalAppManager = rootAppManager
                val rootNavController = rememberNavController()
                val startScreen: RouteType =
                    if (GlobalAppManager.isUserHavingAuthTokens) AppScreenRoute
                    else AuthGraphRoute

                LaunchedEffect(key1 = true) {
                    GlobalAppManager.eventFlow.collectLatest { event ->
                        when (event) {
                            is RootAppManager.RootUiEvent.LogIn -> {
                                rootNavController.navigate(AppScreenRoute) {
                                    popUpTo(rootNavController.graph.id){
                                        inclusive = true
                                    }
                                }
                            }
                            is RootAppManager.RootUiEvent.LogOut -> {
                                rootNavController.navigate(AuthGraphRoute) {
                                    popUpTo(rootNavController.graph.id){
                                        inclusive = false
                                    }
                                }
                            }
                        }
                    }
                }

                NavHost(
                    navController = rootNavController,
                    startDestination = startScreen,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    authGraph(rootNavController)
                    composable<AppScreenRoute> {
                        AppScreen()
                    }
                }
            }
        }
    }
}

@Serializable
object RootGraphRoute : RouteType


/**
 * Authentication is delegated to this viewmodel, because we have nested [NavHost] and
 * using a single NavHostController is not possible
 */
lateinit var GlobalAppManager: RootAppManager