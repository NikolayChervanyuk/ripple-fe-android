package com.mobi.ripple.core.util.paging

open class PageLoadState() {
    data object Loading : PageLoadState()
    data object Success : PageLoadState()
    data object Error : PageLoadState()
    data object Idle : PageLoadState()
}