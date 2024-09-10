package com.mobi.ripple.feature_app.feature_search.presentation.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mobi.ripple.feature_app.feature_search.presentation.model.SearchSimpleUserModel

data class SearchState(
    var foundUsersList: SnapshotStateList<SearchSimpleUserModel> = mutableStateListOf()
)