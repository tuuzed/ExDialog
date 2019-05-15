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

    fun titleColor(@ColorRes colorRes: Int? = null, @ColorInt color: Int? = null)
    fun titleVisible(visible: Boolean = true)
    fun title(@StringRes textRes: Int? = null, text: CharSequence? = null)

    fun positiveButtonColor(@ColorRes colorRes: Int? = null, @ColorInt color: Int? = null)
    fun positiveButtonIcon(@DrawableRes iconRes: Int? = null, icon: Drawable? = null)
    fun positiveButtonEnable(enable: Boolean = true)
    fun positiveButtonVisible(visible: Boolean = true)

    fun positiveButton(
        @StringRes textRes: Int? = android.R.string.ok, text: CharSequence? = null,
        click: DialogButtonClick? = { it.dismiss() }
    )

    fun negativeButtonColor(@ColorRes colorRes: Int? = null, @ColorInt color: Int? = null)
    fun negativeButtonIcon(@DrawableRes iconRes: Int? = null, icon: Drawable? = null)
    fun negativeButtonEnable(enable: Boolean = true)
    fun negativeButtonVisible(visible: Boolean = true)

    fun negativeButton(
        @StringRes textRes: Int? = android.R.string.cancel, text: CharSequence? = null,
        click: DialogButtonClick? = { it.dismiss() }
    )

    fun neutralButtonColor(@ColorRes colorRes: Int? = null, @ColorInt color: Int? = null)
    fun neutralButtonIcon(@DrawableRes iconRes: Int? = null, icon: Drawable? = null)
    fun neutralButtonEnable(enable: Boolean = true)
    fun neutralButtonVisible(visible: Boolean = true)

    fun neutralButton(
        @StringRes textRes: Int? = null, text: CharSequence? = null,
        click: DialogButtonClick? = { it.dismiss() }
    )


}