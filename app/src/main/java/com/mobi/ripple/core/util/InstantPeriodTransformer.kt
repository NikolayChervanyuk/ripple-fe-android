package com.mobi.ripple.core.util

import timber.log.Timber
import java.time.Instant

class InstantPeriodTransformer {

    companion object {
        fun transformToPassedTimeString(date: Instant): String {
            val currentEpochSeconds = Instant.now().epochSecond
            val passedTimeSeconds = currentEpochSeconds - date.epochSecond

            if (passedTimeSeconds < 0) {
                Timber.e("Passed time can't be negative")
            }
            if(passedTimeSeconds == 0L) return "0${Period.entries[0].periodString}"

            var i = Period.entries.size - 1
            var periodNumber = passedTimeSeconds / Period.entries[i].periodDurationSeconds

            while (periodNumber == 0L) {
                periodNumber = passedTimeSeconds / Period.entries[--i].periodDurationSeconds
            }
            return "$periodNumber${Period.entries[i].periodString}"
        }
    }

    private enum class Period(val periodDurationSeconds: Long, val periodString: String) {
        SECOND(1, "s"),
        MINUTE(60, "m"),
        HOUR(3600, "h"),
        DAY(86400, "d"),
        WEEK(604800, "w"),
        YEAR(31536026, "y");

    }
}