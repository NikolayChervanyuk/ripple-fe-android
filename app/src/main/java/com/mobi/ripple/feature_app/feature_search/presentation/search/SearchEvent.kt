package com.mobi.ripple.feature_app.feature_search.presentation.search

sealed class SearchEvent {
  data class SearchTextChanged(val newText: String): SearchEvent()
  data class UserItemClicked(val userId: String, val username: String) : SearchEvent()
}