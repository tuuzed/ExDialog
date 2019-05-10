package com.tuuzed.androidx.dialog.util

import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.View

internal fun View.dp(value: Float): Int {
    return (TypedValue.applyDimension(COMPLEX_UNIT_DIP, value, resources.displayMetrics) + 0.5).toInt()
}

internal fun View.sp(value: Float): Float {
    return TypedValue.applyDimension(COMPLEX_UNIT_SP, value, resources.displayMetrics)
}