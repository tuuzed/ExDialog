package com.tuuzed.androidx.dialog.ext

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

internal interface DialogConfiguratorInterface {
    fun icon(@DrawableRes resId: Int)
    fun icon(icon: Drawable? = null)
    fun title(text: CharSequence? = null, @ColorInt color: Int? = null)
    fun positiveButton(
        text: CharSequence? = null, @ColorInt color: Int? = null,
        icon: Drawable? = null,
        click: ButtonClick = { dialog, _ -> dialog.dismiss() }
    )

    fun negativeButton(
        text: CharSequence? = null, @ColorInt color: Int? = null,
        icon: Drawable? = null,
        click: ButtonClick = { dialog, _ -> dialog.dismiss() }
    )

    fun neutralButton(
        text: CharSequence? = null, @ColorInt color: Int? = null,
        icon: Drawable? = null,
        click: ButtonClick = { dialog, _ -> dialog.dismiss() }
    )

}