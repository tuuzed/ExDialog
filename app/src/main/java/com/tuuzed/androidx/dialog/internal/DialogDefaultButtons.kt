package com.tuuzed.androidx.dialog.internal

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.google.android.material.button.MaterialButton
import com.tuuzed.androidx.dialog.R

class DialogDefaultButtons @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), DialogButtons {

    private val buttonPositive: MaterialButton
    private val buttonNegative: MaterialButton
    private val buttonNeutral: MaterialButton

    init {
        View.inflate(context, R.layout.basic_dialog_layout_part_defaultbuttons, this)

        this.orientation = HORIZONTAL

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

    override fun setPositiveButton(
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: OnClickListener?
    ) = setup(buttonPositive, text, color, icon, click)

    override fun setNegativeButton(
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: OnClickListener?
    ) = setup(buttonNegative, text, color, icon, click)

    override fun setNeutralButton(
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: OnClickListener?
    ) = setup(buttonNeutral, text, color, icon, click)


    private fun setup(
        button: MaterialButton,
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: OnClickListener?
    ) {
        button.visibility = View.VISIBLE
        text?.also { button.text = text }
        color?.also {
            button.setTextColor(it)
            MaterialButtonCompat.setRippleColor(button, it)
        }
        icon?.also { button.icon = icon }
        button.setOnClickListener(click)
    }

    override fun disablePositiveButton(disable: Boolean) {
        buttonPositive.isEnabled = !disable
    }

    override fun disableNegativeButton(disable: Boolean) {
        buttonNegative.isEnabled = !disable
    }

    override fun disableNeutralButton(disable: Boolean) {
        buttonNeutral.isEnabled = !disable
    }

}