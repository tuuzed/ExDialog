package com.tuuzed.androidx.dialog.internal

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.button.MaterialButton
import com.tuuzed.androidx.dialog.ext.*

object MaterialButtonCompat {

    @JvmStatic
    fun setRippleColor(button: MaterialButton, @ColorInt color: Int) {
        button.rippleColor = colorStateList(color)
    }

    private fun colorStateList(@ColorInt color: Int): ColorStateList {
        return ColorStateList(
            // state
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_focused, android.R.attr.state_hovered),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_hovered),
                intArrayOf()
            ),
            // colors
            intArrayOf(
                argbColor(
                    0.16f,
                    colorRed(color),
                    colorGreen(color),
                    colorBlue(color)
                ),
                argbColor(
                    0.12f,
                    colorRed(color),
                    colorGreen(color),
                    colorBlue(color)
                ),
                argbColor(
                    0.12f,
                    colorRed(color),
                    colorGreen(color),
                    colorBlue(color)
                ),
                argbColor(
                    0.04f,
                    colorRed(color),
                    colorGreen(color),
                    colorBlue(color)
                ),
                argbColor(
                    0.00f,
                    colorRed(color),
                    colorGreen(color),
                    colorBlue(color)
                )
            )
        )
    }

}