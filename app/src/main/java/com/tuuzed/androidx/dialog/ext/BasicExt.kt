@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.internal.DialogActionButtonLayout
import com.tuuzed.androidx.dialog.internal.DialogTitle

@SuppressLint("InflateParams")
inline fun ExDialog.basic(func: BasicOptions.() -> Unit) {
    val inflater = LayoutInflater.from(windowContext)

    val dialogView = inflater.inflate(R.layout.basic_dialog_layout, null, false)

    val dialogTitle: DialogTitle = dialogView.findViewById(R.id.dialog_title)
    val dialogActionButtons: DialogActionButtonLayout = dialogView.findViewById(R.id.dialog_action_buttons)
    val dialogCustomViewLayout: FrameLayout = dialogView.findViewById(R.id.dialog_customview_layout)

    dialogTitle.setupHideViews()
    dialogActionButtons.setupHideViews()

    val options = BasicOptions(this, dialogTitle, dialogActionButtons, dialogCustomViewLayout)
    func(options)
    this.setContentView(dialogView)
}

class BasicOptions(
    private val dialog: ExDialog,
    private val dialogTitle: DialogTitle,
    private val dialogActionButtons: DialogActionButtonLayout,
    private val dialogCustomViewLayout: FrameLayout
) : DialogOptionsInterface {

    fun customView(@LayoutRes layoutId: Int) {
        LayoutInflater.from(dialog.windowContext).inflate(layoutId, dialogCustomViewLayout, true)
    }

    fun customView(view: View) {
        dialogCustomViewLayout.removeAllViews()
        dialogCustomViewLayout.addView(view)
    }

    override fun icon(icon: Drawable?) {
        dialogTitle.visibility = View.VISIBLE
        dialogTitle.setIcon(icon)
    }

    override fun title(text: CharSequence?, @ColorInt color: Int?) {
        dialogTitle.visibility = View.VISIBLE
        dialogTitle.setText(text, color)
    }

    override fun positiveButton(text: CharSequence?, @ColorInt color: Int?, icon: Drawable?, click: ButtonClick) {
        dialogActionButtons.visibility = View.VISIBLE
        dialogActionButtons.setPositiveButton(
            text, color, icon,
            View.OnClickListener { click(dialog, ExDialog.BUTTON_POSITIVE) }
        )
    }

    override fun negativeButton(text: CharSequence?, @ColorInt color: Int?, icon: Drawable?, click: ButtonClick) {
        dialogActionButtons.visibility = View.VISIBLE
        dialogActionButtons.setNegativeButton(
            text, color, icon,
            View.OnClickListener { click(dialog, ExDialog.BUTTON_NEGATIVE) }
        )
    }

    override fun neutralButton(text: CharSequence?, @ColorInt color: Int?, icon: Drawable?, click: ButtonClick) {
        dialogActionButtons.visibility = View.VISIBLE
        dialogActionButtons.setNeutralButton(
            text, color, icon,
            View.OnClickListener { click(dialog, ExDialog.BUTTON_NEUTRAL) }
        )
    }

}