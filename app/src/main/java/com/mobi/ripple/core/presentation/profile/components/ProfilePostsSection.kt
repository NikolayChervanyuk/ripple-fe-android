package com.mobi.ripple.core.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.mobi.ripple.core.presentation.components.CircularProgressIndicatorRow
import com.mobi.ripple.core.presentation.components.DefaultCircularProgressIndicator
import com.mobi.ripple.core.presentation.profile.model.UserProfileSimplePostModel
import kotlinx.coroutines.flow.Flow

@Composable
fun ProfilePostsSection(
    onPostClicked: (index: Int, simplePostModel: UserProfileSimplePostModel) -> Unit,
    postsFlow: Flow<PagingData<UserProfileSimplePostModel>>
) {
    val postsLazyPagingItems = postsFlow.collectAsLazyPagingItems()
    if (postsLazyPagingItems.itemCount > 0) {

        if (postsLazyPagingItems.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicatorRow()
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(
                    count = postsLazyPagingItems.itemCount,
                    key = postsLazyPagingItems.itemKey { it.id },
                ) { index ->
                    val item = postsLazyPagingItems[index]
                    SimplePostItem(
                        imageBitmap = item?.image,
                        onClicked = { item?.let { onPostClicked(index, it) } }
                    )
                }
            }
            if (postsLazyPagingItems.loadState.append is LoadState.Loading) {
                CircularProgressIndicatorRow()
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