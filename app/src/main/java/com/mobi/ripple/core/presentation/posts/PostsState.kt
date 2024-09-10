package com.mobi.ripple.core.presentation.posts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.mobi.ripple.core.presentation.post.model.PostModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PostsState(
    val postsFlowState: MutableState<Flow<PagingData<PostModel>>> = mutableStateOf(emptyFlow())
)