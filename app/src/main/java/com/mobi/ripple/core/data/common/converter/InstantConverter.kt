package com.mobi.ripple.core.data.common.converter

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun instantToMilliseconds(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun millisecondsToInstant(milliseconds: Long?): Instant? {
        return milliseconds?.let { Instant.ofEpochMilli(milliseconds) }
    }
}