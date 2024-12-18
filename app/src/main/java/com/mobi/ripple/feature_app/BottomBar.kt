package com.mobi.ripple.feature_app

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.R
import com.mobi.ripple.core.presentation.components.PictureFrame
import com.mobi.ripple.core.presentation.components.RippleBadge
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.core.util.RouteType
import com.mobi.ripple.feature_app.feature_explore.presentation.screens.ExploreScreenRoute
import com.mobi.ripple.feature_app.feature_feed.presentation.screens.FeedScreenRoute
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.PersonalProfileGraphRoute
import com.mobi.ripple.feature_app.feature_search.presentation.search.SearchScreenRoute
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

private val bottomNavBarIconSize = 21.dp

private sealed class BottomNavItem {
    data class BottomNavItemData(
        val title: String,
        val selectedIcon: @Composable () -> Unit,
        val unselectedIcon: @Composable () -> Unit,
        val screenRoute: RouteType,
        var hasNews: Boolean,
        var badgeCount: Int? = null
    )

    companion object {
        val FeedItem: BottomNavItemData =
            BottomNavItemData(
                title = "Home",
                selectedIcon =
                {
                    Icon(
                        modifier = Modifier.size(bottomNavBarIconSize),
                        painter = painterResource(id = R.drawable.homepage_icon),
                        tint = colorScheme.primary,
                        contentDescription = "Home item selected"
                    )
                },
                unselectedIcon = {
                    Icon(
                        modifier = Modifier.size(bottomNavBarIconSize),
                        painter = painterResource(id = R.drawable.homepage_icon),
                        tint = colorScheme.onSurface,
                        contentDescription = "Home item"
                    )
                },
                hasNews = false,
                screenRoute = FeedScreenRoute
            )
        val SearchItem: BottomNavItemData =
            BottomNavItemData(
                title = "Search",
                selectedIcon =
                {
                    Icon(
                        modifier = Modifier.size(bottomNavBarIconSize),
                        painter = painterResource(id = R.drawable.search_icon),
                        tint = colorScheme.primary,
                        contentDescription = "Search item selected"
                    )
                },
                unselectedIcon = {
                    Icon(
                        modifier = Modifier.size(bottomNavBarIconSize),
                        painter = painterResource(id = R.drawable.search_icon),
                        tint = colorScheme.onSurface,
                        contentDescription = "Search item"
                    )
                },
                hasNews = false,
                screenRoute = SearchScreenRoute
            )

        val ExploreItem: BottomNavItemData =
            BottomNavItemData(
                title = "Explore",
                selectedIcon =
                {
                    Icon(
                        modifier = Modifier.size(bottomNavBarIconSize),
                        painter = painterResource(id = R.drawable.compass_icon),
                        tint = colorScheme.primary,
                        contentDescription = "Explore item selected"
                    )
                },
                unselectedIcon = {
                    Icon(
                        modifier = Modifier.size(bottomNavBarIconSize),
                        painter = painterResource(id = R.drawable.compass_icon),
                        tint = colorScheme.onSurface,
                        contentDescription = "Explore item"
                    )
                },
                hasNews = false,
                screenRoute = ExploreScreenRoute
            )
        val ProfileItem: BottomNavItemData =
            BottomNavItemData(
                title = "Profile",
                selectedIcon =
                {
                    Icon(
                        modifier = Modifier.size(bottomNavBarIconSize),
                        painter = painterResource(id = R.drawable.user_profile_btn),
                        tint = colorScheme.primary,
                        contentDescription = "Profile item selected"
                    )
                },
                unselectedIcon = {
                    Icon(
                        modifier = Modifier.size(bottomNavBarIconSize),
                        painter = painterResource(id = R.drawable.user_profile_btn),
                        tint = colorScheme.onSurface,
                        contentDescription = "Profile item"
                    )
                },
                hasNews = false,
                screenRoute = PersonalProfileGraphRoute
            )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val outlineColor = colorScheme.outline
    val navItems = listOf(
        BottomNavItem.FeedItem,
        BottomNavItem.SearchItem,
        BottomNavItem.ExploreItem,
        BottomNavItem.ProfileItem,
    )
    val selectedItemIndex = rememberSaveable {
        mutableIntStateOf(0)
    }
    val navStackBackEntry by navController.currentBackStackEntryAsState()

    val coroutineScope = rememberCoroutineScope()
    val personalPfp = remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            personalPfp.value = GlobalAppManager.getSmallProfilePicture()?.let {
                val outputStream = ByteArrayOutputStream()
                BitmapUtils.convertImageByteArrayToBitmap(it)
                    .compress(Bitmap.CompressFormat.JPEG, 20, outputStream)
                BitmapUtils.convertImageByteArrayToBitmap(outputStream.toByteArray())
                    .asImageBitmap()
            }
        }
    }

    LaunchedEffect(key1 = navStackBackEntry) {
        when (navStackBackEntry?.destination?.route) {
            FeedScreenRoute.javaClass.name -> selectedItemIndex.intValue = 0
            SearchScreenRoute.javaClass.name -> selectedItemIndex.intValue = 1
            ExploreScreenRoute.javaClass.name -> selectedItemIndex.intValue = 2
            PersonalProfileGraphRoute.javaClass.name -> selectedItemIndex.intValue = 3
        }
    }
    NavigationBar(
        Modifier
            .height(50.dp)
            .drawBehind {
                drawLine(
                    color = outlineColor,
                    start = Offset.Zero,
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            },
        containerColor = colorScheme.background,
        windowInsets = WindowInsets.systemBars
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(selected = selectedItemIndex.intValue == index,
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors()
                    .copy(selectedIndicatorColor = Color.Transparent),
                onClick = {
                    navController.navigate(item.screenRoute)
                    selectedItemIndex.intValue = index

                },
                icon = {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        //Icon TODO: show pfp image
                        if (index == 3) {
                            val borderColor = colorScheme.primary
                            personalPfp.value?.let {
                                PictureFrame(
                                    modifier = Modifier.size(bottomNavBarIconSize + 7.dp),
                                    picture = personalPfp.value,
                                    borderColor = borderColor,
                                    borderWidthDp = if (
                                        selectedItemIndex.intValue == index
                                    ) 2.dp else 0.dp
                                )
                            } ?: if (index == selectedItemIndex.intValue) item.selectedIcon()
                            else item.unselectedIcon()
                        }
                        else
                        if ((index == selectedItemIndex.intValue)) {
                            item.selectedIcon()
                        } else item.unselectedIcon()
                        //Badge
                        RippleBadge(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 10.dp, y = (-5).dp)
                                .scale(0.8f),
                            hasNews = item.hasNews,
                            counter = item.badgeCount
                        )
                    }
                }
            )
        }
    }
}