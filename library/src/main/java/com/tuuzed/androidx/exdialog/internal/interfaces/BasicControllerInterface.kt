package com.tuuzed.androidx.exdialog.internal.interfaces

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ext.DialogButtonClick

internal interface BasicControllerInterface {
    fun icon(@DrawableRes resId: Int? = null, icon: Drawable? = null)
    fun title(@StringRes resId: Int? = null, text: CharSequence? = null, @ColorInt color: Int? = null)

    fun positiveButton(
        text: CharSequence, @ColorInt color: Int? = null, icon: Drawable? = null,
        click: DialogButtonClick = { dialog, _ -> dialog.dismiss() }
    )

    fun negativeButton(
        text: CharSequence, @ColorInt color: Int? = null, icon: Drawable? = null,
        click: DialogButtonClick = { dialog, _ -> dialog.dismiss() }
    )

    fun neutralButton(
        text: CharSequence, @ColorInt color: Int? = null, icon: Drawable? = null,
        click: DialogButtonClick = { dialog, _ -> dialog.dismiss() }
    )

    fun disablePositiveButton(disable: Boolean = true)
    fun disableNegativeButton(disable: Boolean = true)
    fun disableNeutralButton(disable: Boolean = true)

}