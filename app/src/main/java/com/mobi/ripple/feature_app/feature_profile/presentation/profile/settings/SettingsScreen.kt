package com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.DefaultDialog
import com.mobi.ripple.core.presentation.DefaultDialogHeader
import com.mobi.ripple.core.presentation.DefaultHorizontalDivider
import com.mobi.ripple.core.presentation.OptionItem
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.ProfileScreenRoute
import kotlinx.serialization.Serializable

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    DefaultDialog(
        header = {
            DefaultDialogHeader(
                onBackButtonClicked = {
                    navController.popBackStack(ProfileScreenRoute, false)
                },
                title = "Settings"
            )
        },
        onDismissRequest = {
            navController.popBackStack(ProfileScreenRoute, false)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            EditProfileOption {
                navController.navigate(EditProfileScreenRoute)
            }
            LogoutOption {
                viewModel.onEvent(SettingsEvent.LogoutRequested)
            }
            LogoutAllDevicesOption {
                viewModel.onEvent(SettingsEvent.LogoutAllDevicesRequested)
            }
        }
    }
}

@Composable
private fun EditProfileOption(onClick: () -> Unit) {
    OptionItem(iconId = R.drawable.edit_pen_icon, text = "Edit profile") {
        onClick()
    }
}

@Composable
private fun LogoutOption(onClick: () -> Unit) {
    DefaultHorizontalDivider(modifier = Modifier.clip(CircleShape), thickness = 1.dp)
    OptionItem(iconId = R.drawable.sign_out_icon, text = "Log out") {
        onClick()
    }
}

@Composable
private fun LogoutAllDevicesOption(onClick: () -> Unit) {
    OptionItem(
        iconId = R.drawable.sign_out_icon,
        color = MaterialTheme.colorScheme.error,
        text = "Log out from all devices"
    ) {
        onClick()
    }
}

@Serializable
object SettingsScreenRoute : RouteType