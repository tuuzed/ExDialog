package com.tuuzed.androidx.exdialog.internal

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.ext.dp

class DialogButtonLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    val positiveButton: MaterialButton
    val negativeButton: MaterialButton
    val neutralButton: MaterialButton

    init {
        View.inflate(context, R.layout.basic_dialog_layout_part_buttons, this)

        this.orientation = HORIZONTAL
        setPadding(dp(12f), 0, dp(12f), 0)

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dp(48f))

        positiveButton = findViewById(R.id.buttonPositive)
        negativeButton = findViewById(R.id.buttonNegative)
        neutralButton = findViewById(R.id.buttonNeutral)

    }

    fun requestUpdateLayout() {
        if (positiveButton.visibility == View.VISIBLE ||
            negativeButton.visibility == View.VISIBLE ||
            neutralButton.visibility == View.VISIBLE
        ) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

}