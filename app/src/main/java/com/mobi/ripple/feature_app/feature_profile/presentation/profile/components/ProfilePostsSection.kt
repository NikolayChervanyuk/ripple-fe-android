package com.mobi.ripple.feature_app.feature_profile.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.theme.RippleTheme
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.UserProfileSimplePostModel

@Composable
fun ProfilePostsSection(posts: MutableList<UserProfileSimplePostModel>) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (posts.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                posts.onEach { post ->
                    item {
                        SimplePostItem(
                            imageData = post.image,
                            onClicked = {/*TODO: open the post*/ }
                        )
                    }
                }
            }
        } else {
            Text(
                text = "No posts uploaded",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePostsSectionPreview() {
    RippleTheme {
        ProfilePostsSection(mutableListOf())
    }
}