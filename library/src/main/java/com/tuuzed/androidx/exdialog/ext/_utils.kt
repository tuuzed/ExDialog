package com.tuuzed.androidx.exdialog.ext

import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.FloatRange
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val DatePicker.dateFormat: DateFormat
    get() {
        return when (this.type) {
            DatePickerType.TYPE_Y -> SimpleDateFormat("yyyy", Locale.CHINA)
            DatePickerType.TYPE_YM -> SimpleDateFormat("yyyy-MM", Locale.CHINA)
            DatePickerType.TYPE_YMD -> SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            DatePickerType.TYPE_YMDH -> SimpleDateFormat("yyyy-MM-dd HH", Locale.CHINA)
            DatePickerType.TYPE_ALL -> SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
            else -> SimpleDateFormat("yyyy-MM-hh HH:mm", Locale.CHINA)
        }
    }

internal fun View.dp(value: Float): Int {
    return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics) + 0.5).toInt()
}

internal fun View.sp(value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)
}

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