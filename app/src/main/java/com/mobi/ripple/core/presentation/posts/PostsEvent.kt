package com.mobi.ripple.core.presentation.posts

sealed class PostsEvent {
  data class InitPostsList(val startItemIndex: Int, val authorId: String): PostsEvent()
//  data object AnotherEvent: PostsEvent()
}