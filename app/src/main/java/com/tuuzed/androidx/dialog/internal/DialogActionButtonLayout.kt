package com.tuuzed.androidx.dialog.internal

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.google.android.material.button.MaterialButton
import com.tuuzed.androidx.dialog.R

class DialogActionButtonLayout(
    context: Context, attrs: AttributeSet
) : LinearLayout(context, attrs) {

    private val buttonPositive: MaterialButton
    private val buttonNegative: MaterialButton
    private val buttonNeutral: MaterialButton

    init {

        View.inflate(context, R.layout.basic_dialog_layout_part_actionbuttons, this)

        buttonPositive = findViewById(R.id.buttonPositive)
        buttonNegative = findViewById(R.id.buttonNegative)
        buttonNeutral = findViewById(R.id.buttonNeutral)
    }

    fun setupHideViews() {
        this.visibility = View.GONE
        buttonPositive.visibility = View.GONE
        buttonPositive.visibility = View.GONE
        buttonNeutral.visibility = View.GONE
    }


    fun setPositiveButton(text: CharSequence?, @ColorInt color: Int?, icon: Drawable?, click: OnClickListener?) =
        buttonPositive.setup(text, color, icon, click)

    fun setNegativeButton(text: CharSequence?, @ColorInt color: Int?, icon: Drawable?, click: OnClickListener?) =
        buttonNegative.setup(text, color, icon, click)

    fun setNeutralButton(text: CharSequence?, @ColorInt color: Int?, icon: Drawable?, click: OnClickListener?) =
        buttonNeutral.setup(text, color, icon, click)

    private fun MaterialButton.setup(
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: OnClickListener?
    ) {
        this.visibility = View.VISIBLE
        text?.also { this.text = text }
        color?.also {
            this.setTextColor(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.rippleColor = ColorStateList.valueOf(it)
            } else {
                // TODO
            }
            icon?.also { this.icon = icon }
        }
        this.setOnClickListener(click)

    }
}