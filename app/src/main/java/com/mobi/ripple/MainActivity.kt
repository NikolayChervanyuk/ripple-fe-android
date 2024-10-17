package com.mobi.ripple

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mobi.ripple.core.config.BuildConfig
import com.mobi.ripple.core.theme.OnBackgroundDarkBlue
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.core.util.invalidateBearerTokens
import com.mobi.ripple.feature_app.AppScreen
import com.mobi.ripple.feature_app.AppScreenRoute
import com.mobi.ripple.feature_app.MessageManager
import com.mobi.ripple.feature_app.feature_chat.service.scheduled.PendingMessagesWorker
import com.mobi.ripple.feature_auth.presentation.AuthGraphRoute
import com.mobi.ripple.feature_auth.presentation.authGraph
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import timber.log.Timber
import java.time.Duration
import javax.inject.Inject

var isAppClosed: Boolean = true

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootAppManager: RootAppManager

    @Inject
    lateinit var client: HttpClient

    @Inject
    lateinit var messageManager: MessageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(
                OnBackgroundDarkBlue.value.toInt(), OnBackgroundDarkBlue.value.toInt()
            )
        )
        if (BuildConfig.DEBUG) {
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
                                client.invalidateBearerTokens()
                                rootNavController.navigate(AppScreenRoute) {
                                    popUpTo(rootNavController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }

                            is RootAppManager.RootUiEvent.LogOut -> {
                                client.invalidateBearerTokens()
                                rootNavController.navigate(AuthGraphRoute) {
                                    popUpTo(rootNavController.graph.id) {
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
                        AppScreen(messageManager)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        isAppClosed = false
    }

    override fun onStop() {
        super.onStop()
        isAppClosed = true
        val request = PeriodicWorkRequestBuilder<PendingMessagesWorker>(Duration.ofMinutes(15))
            .addTag(PendingMessagesWorker.TAG_NAME)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(request)
    }
}

@Serializable
object RootGraphRoute : RouteType


/**
 * Authentication is delegated to this viewmodel, because we have nested [NavHost] and
 * using a single NavHostController is not possible
 */
lateinit var GlobalAppManager: RootAppManager

//lateinit var GlobalMessageManager: MessageManager