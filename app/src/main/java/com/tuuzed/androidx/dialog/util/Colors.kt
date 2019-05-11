package com.tuuzed.androidx.dialog.util

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange

internal fun colorRed(@ColorInt color: Int) = (color shr 16 and 0xff) / 255.0f
internal fun colorGreen(@ColorInt color: Int) = (color shr 8 and 0xff) / 255.0f
internal fun colorBlue(@ColorInt color: Int) = (color and 0xff) / 255.0f
internal fun colorAlpha(@ColorInt color: Int) = (color shr 24 and 0xff) / 255.0f


internal fun argbColor(
    @IntRange(from = 0, to = 255) a: Int,
    @IntRange(from = 0, to = 255) r: Int,
    @IntRange(from = 0, to = 255) g: Int,
    @IntRange(from = 0, to = 255) b: Int
): Int {
    return a shl 24 or (r shl 16) or (g shl 8) or b
}

internal fun argbColor(
    @FloatRange(from = 0.0, to = 1.0) a: Float,
    @FloatRange(from = 0.0, to = 1.0) r: Float,
    @FloatRange(from = 0.0, to = 1.0) g: Float,
    @FloatRange(from = 0.0, to = 1.0) b: Float
): Int {
    return (a * 255.0f + 0.5f).toInt() shl 24 or
            ((r * 255.0f + 0.5f).toInt() shl 16) or
            ((g * 255.0f + 0.5f).toInt() shl 8) or
            (b * 255.0f + 0.5f).toInt()
}