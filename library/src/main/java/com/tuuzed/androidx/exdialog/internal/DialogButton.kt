package com.tuuzed.androidx.exdialog.internal

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.google.android.material.button.MaterialButton
import com.tuuzed.androidx.exdialog.R

class DialogButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    init {
        var dialogButtonColor = -1
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.DialogButton)
            dialogButtonColor = a.getColor(R.styleable.DialogButton_dialog_button_color, -1)
            a.recycle()
        }
        if (dialogButtonColor != -1) {
            setButtonColor(dialogButtonColor)
        }
    }

    fun setButtonColor(@ColorInt color: Int) {
        MaterialButtonCompat.setTextColor(this, color)
        MaterialButtonCompat.setRippleColor(this, color)
    }
}