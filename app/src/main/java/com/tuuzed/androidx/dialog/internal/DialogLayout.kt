package com.tuuzed.androidx.dialog.internal

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.DialogTitle
import com.tuuzed.androidx.dialog.R

class DialogLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var dialogTitle: DialogTitle? = null
    private var dialogContent: FrameLayout? = null
    private var dialogDefaultButtons: DialogButtonLayout? = null

    init {
        dialogTitle = findViewById(R.id.dialog_title)
        dialogContent = findViewById(R.id.dialog_content_layout)
        dialogDefaultButtons = findViewById(R.id.dialog_buttons)
    }
}