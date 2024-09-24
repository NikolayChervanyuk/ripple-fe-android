package com.mobi.ripple.core.presentation.posts

import androidx.paging.PagingData
import com.mobi.ripple.core.presentation.posts.model.PostItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PostsState(
    var postsFlowState: Flow<PagingData<PostItemModel>> = emptyFlow()
) {

}