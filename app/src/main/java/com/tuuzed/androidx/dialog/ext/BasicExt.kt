@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import com.tuuzed.androidx.dialog.internal.DialogDefaultButtons
import com.tuuzed.androidx.dialog.internal.DialogTitle

inline fun ExDialog.Factory.basic(windowContext: Context, func: BasicController.() -> Unit) {
    ExDialog.show(windowContext) { basic(func) }
}

inline fun ExDialog.basic(func: BasicController.() -> Unit) {
    func(BasicController(this) { setContentView(it) })
}


class BasicController(
    private val dialog: ExDialog,
    attachView: (View) -> Unit
) : ExDialogInterface by dialog,
    BasicControllerInterface {

    private val dialogTitle: DialogTitle
    private val dialogButtons: DialogDefaultButtons
    private val dialogCustomViewLayout: FrameLayout

    init {

        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.basic_dialog_layout, null, false)

        dialogTitle = view.findViewById(R.id.dialog_title)
        dialogButtons = view.findViewById(R.id.dialog_buttons)
        dialogCustomViewLayout = view.findViewById(R.id.dialog_customview_layout)

        dialogTitle.setupHideViews()
        dialogButtons.setupHideViews()
        attachView(view)
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

    override fun positiveButton(
        text: CharSequence, @ColorInt color: Int?,
        icon: Drawable?,
        click: DialogButtonClick?
    ) {
        dialogButtons.visibility = View.VISIBLE
        dialogButtons.setPositiveButton(
            text, color, icon,
            View.OnClickListener { click?.invoke(dialog, ExDialog.BUTTON_POSITIVE) }
        )
    }

    override fun negativeButton(
        text: CharSequence, @ColorInt color: Int?,
        icon: Drawable?,
        click: DialogButtonClick?
    ) {
        dialogButtons.visibility = View.VISIBLE
        dialogButtons.setNegativeButton(
            text, color, icon,
            View.OnClickListener { click?.invoke(dialog, ExDialog.BUTTON_NEGATIVE) }
        )
    }

    override fun neutralButton(
        text: CharSequence, @ColorInt color: Int?,
        icon: Drawable?,
        click: DialogButtonClick?
    ) {
        dialogButtons.visibility = View.VISIBLE
        dialogButtons.setNeutralButton(
            text, color, icon,
            View.OnClickListener { click?.invoke(dialog, ExDialog.BUTTON_NEUTRAL) }
        )
    }

    override fun disablePositiveButton(disable: Boolean) {
        dialogButtons.disablePositiveButton(disable)
    }

    override fun disableNegativeButton(disable: Boolean) {
        dialogButtons.disableNegativeButton(disable)
    }

    override fun disableNeutralButton(disable: Boolean) {
        dialogButtons.disableNeutralButton(disable)
    }

}