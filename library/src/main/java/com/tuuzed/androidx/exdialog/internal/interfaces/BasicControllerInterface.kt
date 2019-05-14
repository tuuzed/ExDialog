package com.tuuzed.androidx.exdialog.internal.interfaces

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ext.DialogButtonClick

interface BasicControllerInterface {

    fun icon(
        @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
        visible: Boolean = true
    )

    fun title(
        @StringRes textRes: Int? = null, text: CharSequence? = null,
        @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null,
        visible: Boolean = true
    )

    fun positiveButton(
        @StringRes textRes: Int? = android.R.string.ok, text: CharSequence? = null,
        @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null,
        @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
        enable: Boolean = true,
        visible: Boolean = true,
        click: DialogButtonClick = { it.dismiss() }
    )

    fun negativeButton(
        @StringRes textRes: Int? = android.R.string.cancel, text: CharSequence? = null,
        @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null,
        @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
        enable: Boolean = true,
        visible: Boolean = true,
        click: DialogButtonClick = { it.dismiss() }
    )

    fun neutralButton(
        @StringRes textRes: Int? = null, text: CharSequence? = null,
        @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null,
        @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
        enable: Boolean = true,
        visible: Boolean = true,
        click: DialogButtonClick = { it.dismiss() }
    )


}