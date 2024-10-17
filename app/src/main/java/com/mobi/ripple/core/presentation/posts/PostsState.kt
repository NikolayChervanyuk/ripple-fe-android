package com.mobi.ripple.core.presentation.posts

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.post.model.Post
import com.mobi.ripple.core.presentation.post.model.PostModel
//import com.mobi.ripple.core.presentation.posts.model.PostItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PostsState(
    var postsFlowState: Flow<PagingData<PostModel>> = emptyFlow(),
    var firstCachedPost: Post? = null
) {

}