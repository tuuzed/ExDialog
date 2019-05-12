@file:Suppress("JoinDeclarationAndAssignment")

package com.tuuzed.androidx.exdialog.internal

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout

internal class DialogLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val TAG = "DialogLayout"

    val dialogTitle: DialogTitle
    val dialogContent: FrameLayout
    val dialogButtons: DialogButtonLayout

    init {
        dialogTitle = DialogTitle(context).also { addView(it) }
        dialogContent = FrameLayout(context).also {
            it.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).also { lp ->
                    lp.weight = 1f
                }
            addView(it)
        }
        dialogButtons = DialogButtonLayout(context).also { addView(it) }
        Log.v(TAG, "dialogTitle: $dialogTitle")
        Log.v(TAG, "dialogContent: $dialogContent")
        Log.v(TAG, "dialogButtons: $dialogButtons")

    }

}