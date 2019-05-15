package com.tuuzed.androidx.exdialog.internal.interfaces

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ext.DialogButtonClick

interface BasicControllerInterface {

    fun iconVisible(visible: Boolean = true)
    fun icon(@DrawableRes iconRes: Int? = null, icon: Drawable? = null)

    fun titleVisible(visible: Boolean = true)
    fun title(@StringRes textRes: Int? = null, text: CharSequence? = null)

    fun positiveButtonEnable(enable: Boolean = true)
    fun positiveButtonVisible(visible: Boolean = true)
    fun positiveButton(
        @StringRes textRes: Int? = android.R.string.ok, text: CharSequence? = null,
        @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
        @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null,
        click: DialogButtonClick? = { it.dismiss() }
    )

    fun negativeButtonEnable(enable: Boolean = true)
    fun negativeButtonVisible(visible: Boolean = true)
    fun negativeButton(
        @StringRes textRes: Int? = android.R.string.cancel, text: CharSequence? = null,
        @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
        @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null,
        click: DialogButtonClick? = { it.dismiss() }
    )

    fun neutralButtonEnable(enable: Boolean = true)
    fun neutralButtonVisible(visible: Boolean = true)
    fun neutralButton(
        @StringRes textRes: Int? = null, text: CharSequence? = null,
        @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
        @ColorRes colorRes: Int? = null, @ColorInt color: Int? = null,
        click: DialogButtonClick? = { it.dismiss() }
    )


}