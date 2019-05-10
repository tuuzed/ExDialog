@file:Suppress("unused", "CanBeParameter")

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
inline fun ExDialog.basic(func: BasicConfigurator.() -> Unit) {
    val inflater = LayoutInflater.from(windowContext)
    val view = inflater.inflate(R.layout.basic_dialog_layout, null, false)
    val configurator = BasicConfigurator(this, view)
    func(configurator)
    setContentView(view)
}

class BasicConfigurator(
    private val dialog: ExDialog,
    private val view: View
) : DialogConfiguratorInterface {

    private val dialogTitle: DialogTitle = view.findViewById(R.id.dialog_title)
    private val dialogActionButtons: DialogActionButtonLayout = view.findViewById(R.id.dialog_action_buttons)
    private val dialogCustomViewLayout: FrameLayout = view.findViewById(R.id.dialog_customview_layout)

    init {
        dialogTitle.setupHideViews()
        dialogActionButtons.setupHideViews()
    }

    fun customView(@LayoutRes layoutId: Int) {
        LayoutInflater.from(dialog.windowContext).inflate(layoutId, dialogCustomViewLayout, true)
    }

    fun customView(view: View) {
        dialogCustomViewLayout.removeAllViews()
        dialogCustomViewLayout.addView(view)
    }

    override fun icon(resId: Int) {
        dialogTitle.visibility = View.VISIBLE
        dialogTitle.setIcon(resId)
    }

    override fun icon(icon: Drawable?) {
        dialogTitle.visibility = View.VISIBLE
        icon?.also { dialogTitle.setIcon(it) }
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