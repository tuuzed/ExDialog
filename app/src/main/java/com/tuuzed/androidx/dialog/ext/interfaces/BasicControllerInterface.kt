package com.tuuzed.androidx.dialog.ext.interfaces

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.tuuzed.androidx.dialog.ext.DialogButtonClick

interface BasicControllerInterface {
    fun icon(@DrawableRes resId: Int)
    fun icon(icon: Drawable? = null)
    fun title(text: CharSequence? = null, @ColorInt color: Int? = null)

    fun positiveButton(
        text: CharSequence, @ColorInt color: Int? = null,
        icon: Drawable? = null,
        click: DialogButtonClick? = { dialog, _ -> dialog.dismiss() }
    )

    fun negativeButton(
        text: CharSequence, @ColorInt color: Int? = null,
        icon: Drawable? = null,
        click: DialogButtonClick? = { dialog, _ -> dialog.dismiss() }
    )

    fun neutralButton(
        text: CharSequence, @ColorInt color: Int? = null,
        icon: Drawable? = null,
        click: DialogButtonClick? = { dialog, _ -> dialog.dismiss() }
    )

    fun disablePositiveButton(disable: Boolean = true)
    fun disableNegativeButton(disable: Boolean = true)
    fun disableNeutralButton(disable: Boolean = true)

}