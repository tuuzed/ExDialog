package com.tuuzed.androidx.exdialog.internal

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import com.google.android.material.button.MaterialButton
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.ext.dp

internal class DialogButtonLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    @IntDef(
        flag = true, value = [
            BUTTON_POSITIVE,
            BUTTON_NEGATIVE,
            BUTTON_NEUTRAL
        ]
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class DialogButton

    companion object {
        const val BUTTON_POSITIVE = ExDialog.BUTTON_POSITIVE
        const val BUTTON_NEGATIVE = ExDialog.BUTTON_NEGATIVE
        const val BUTTON_NEUTRAL = ExDialog.BUTTON_NEUTRAL
    }

    private val buttonPositive: MaterialButton
    private val buttonNegative: MaterialButton
    private val buttonNeutral: MaterialButton

    init {
        View.inflate(context, R.layout.basic_dialog_layout_part_buttons, this)

        this.orientation = HORIZONTAL
        setPadding(dp(12f), 0, dp(12f), 0)

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dp(48f))

        buttonPositive = findViewById(R.id.buttonPositive)
        buttonNegative = findViewById(R.id.buttonNegative)
        buttonNeutral = findViewById(R.id.buttonNeutral)

        this.visibility = View.GONE
        buttonPositive.visibility = View.GONE
        buttonNegative.visibility = View.GONE
        buttonNeutral.visibility = View.GONE
    }


    fun dialogButton(
        @DialogButton dialogButton: Int,
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: OnClickListener?
    ) {
        when (dialogButton) {
            BUTTON_POSITIVE -> dialogButton(buttonPositive, text, color, icon, click)
            BUTTON_NEGATIVE -> dialogButton(buttonNegative, text, color, icon, click)
            BUTTON_NEUTRAL -> dialogButton(buttonNeutral, text, color, icon, click)
        }
    }

    fun disableDialogButton(
        @DialogButton dialogButton: Int,
        disable: Boolean
    ) {
        when (dialogButton) {
            BUTTON_POSITIVE -> buttonPositive.isEnabled = !disable
            BUTTON_NEGATIVE -> buttonNegative.isEnabled = !disable
            BUTTON_NEUTRAL -> buttonNeutral.isEnabled = !disable
        }
    }

    private fun dialogButton(
        button: MaterialButton,
        text: CharSequence?, @ColorInt color: Int?,
        icon: Drawable?,
        click: OnClickListener?
    ) {
        button.visibility = View.VISIBLE
        text?.also { button.text = text }
        color?.also {
            MaterialButtonCompat.setTextColor(button, it)
            MaterialButtonCompat.setRippleColor(button, it)
        }
        icon?.also { button.icon = icon }
        button.setOnClickListener(click)
    }

}