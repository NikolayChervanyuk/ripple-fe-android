package com.mobi.ripple.core.util

import java.time.Instant

const val DEFAULT_DELAY_MILLI = 300L

fun isDelayPassed(lastInvoked: Instant, delayMilli: Long = DEFAULT_DELAY_MILLI): Boolean {
    return Instant.now().isAfter(lastInvoked.plusMillis(delayMilli))
}