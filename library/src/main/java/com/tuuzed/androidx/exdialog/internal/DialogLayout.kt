package com.tuuzed.androidx.exdialog.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tuuzed.androidx.exdialog.ext.dp

class DialogLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val dialogTitle: DialogTitle = DialogTitle(context).also { addView(it) }
    val dialogContentLayout: FrameLayout = FrameLayout(context).also {
        it.minimumHeight = dp(32f)
        it.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        ).also { lp ->
            lp.weight = 1f
        }
        addView(it)
    }
    val dialogButtonLayout: DialogButtonLayout = DialogButtonLayout(context).also { addView(it) }


}