package com.tuuzed.androidx.exdialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.SparseBooleanArray
import android.view.View
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.forEach
import com.tuuzed.androidx.exdialog.compat.DialogButtonCompat

fun SparseBooleanArray.values(): BooleanArray {
    val values = BooleanArray(this.size())
    forEach { key, value -> values[key] = value }
    return values
}

fun SparseBooleanArray.indices(condition: Boolean): IntArray {
    val indices = mutableListOf<Int>()
    forEach { key, value -> if (value == condition) indices.add(key) }
    return indices.toIntArray()
}

internal fun adjustButtonColor(button: Button?, @ColorInt color: Int?) {
    color?.also {
        DialogButtonCompat.setRippleColor(button, it)
        DialogButtonCompat.setTextColor(button, it)
    }
}

internal fun resolveString(context: Context, @StringRes resId: Int, text: CharSequence? = null): CharSequence? {
    return when {
        text != null -> text
        resId != View.NO_ID -> context.resources.getString(resId)
        else -> null
    }
}

internal fun resolveDrawable(context: Context, @DrawableRes resId: Int, drawable: Drawable?): Drawable? {
    return when {
        drawable != null -> drawable
        resId != View.NO_ID -> ResourcesCompat.getDrawable(context.resources, resId, context.theme)
        else -> null
    }
}

internal fun resolveColor(context: Context, @ColorRes resId: Int, @ColorInt color: Int): Int? {
    return when {
        color != -1 -> color
        resId != View.NO_ID -> ResourcesCompat.getColor(context.resources, resId, context.theme)
        else -> null
    }
}
