package com.mobi.ripple.core.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobi.ripple.core.presentation.profile.model.UserProfileSimplePostModel

@Composable
fun ProfilePostsSection(
    posts: SnapshotStateList<UserProfileSimplePostModel>
) {
    val mutableStateList = remember { posts }

    if (mutableStateList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),

            ) {
            mutableStateList.onEach { post ->
                item {
                    SimplePostItem(
                        imageData = post.image,
                        onClicked = {/*TODO: open the post*/ }
                    )
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "No posts uploaded",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//private fun ProfilePostsSectionPreview() {
//    RippleTheme {
//        ProfilePostsSection(remember { mutableStateListOf() })
//    }
//}