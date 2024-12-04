package com.mobi.ripple.core.util

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import timber.log.Timber

/**
 * Causes loadTokens callback function to be called again
 */
fun HttpClient.invalidateBearerTokens() {
    try {
        plugin(Auth).providers
            .filterIsInstance<BearerAuthProvider>()
            .first().clearToken()
    } catch (e: IllegalStateException) {
        Timber.tag("HttpClient").e("Auth plugin not installed so no tokens were cleared")
    }
}