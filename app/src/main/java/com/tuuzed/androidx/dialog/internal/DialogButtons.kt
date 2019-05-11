package com.tuuzed.androidx.dialog.internal

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt

interface DialogButtons {
    fun setPositiveButton(
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: View.OnClickListener?
    )

    fun setNegativeButton(
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: View.OnClickListener?
    )

    fun setNeutralButton(
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: View.OnClickListener?
    )

    fun disablePositiveButton(disable: Boolean)
    fun disableNegativeButton(disable: Boolean)
    fun disableNeutralButton(disable: Boolean)
}