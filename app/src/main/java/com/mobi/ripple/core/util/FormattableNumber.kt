package com.mobi.ripple.core.util

import kotlin.math.roundToInt

private enum class Magnitude(val label: String) {
    HUNDRED(" "),
    THOUSAND("k"),
    MILLION("M"),
    BILLION("B"),
    TRILLION("T"),
}

class FormattableNumber {
    private var numLong: Long = 0
    private var numFloat: Float = 0.0f
    private var trimAfterComma: Boolean = false
    private var isLong = true

    constructor(number: Long, shouldTrimAfterComma: Boolean = false) {
        numLong = number
        trimAfterComma = shouldTrimAfterComma
    }

    constructor(floatNumber: Float, shouldTrimAfterComma: Boolean = false) {
        numFloat = floatNumber
        trimAfterComma = shouldTrimAfterComma
        isLong = false
    }

    fun format(): String {
        if (isLong) return format(numLong, trimAfterComma)
        return format(numFloat, trimAfterComma)
    }

    companion object {
        fun format(
            number: Long,
            shouldTrimAfterComma: Boolean = false,
            shouldTrimOnZero: Boolean = false
        ): String {
            var num = number
            var magnitude = 0
            var trailingNumber: Long = 1
            while (num / 1000 != 0L && (magnitude + 1) < Magnitude.entries.size) {
                trailingNumber = (num / 100L) % 10L
                num /= 1000
                ++magnitude
            }
            if (magnitude == 0) {
                return num.toString()
            }
            if (shouldTrimAfterComma) return num.toString() + Magnitude.entries[magnitude].label
            return num.toString() + (if (trailingNumber == 0L && shouldTrimOnZero) "" else ".$trailingNumber") +
                    Magnitude.entries[magnitude].label
        }

        fun format(number: Int, shouldTrimAfterComma: Boolean = false): String {
            var num = number
            var magnitude = 0
            var trailingNumber: Int = 1
            while (num / 1000 != 0 && (magnitude + 1) < Magnitude.values().size) {
                trailingNumber = (num / 100) % 10
                num /= 1000
                ++magnitude
            }
            if (magnitude == 0) {
                return num.toString()
            }
            if (shouldTrimAfterComma) return num.toString() + Magnitude.entries[magnitude].label
            return num.toString() + "." + trailingNumber.toString() + Magnitude.entries[magnitude].label
        }

        fun format(number: Float, shouldTrimAfterComma: Boolean = false): String {
            if (shouldTrimAfterComma) return number.roundToInt().toString()
            return ((number * 10).roundToInt() / 10f).toString()
        }
    }
}