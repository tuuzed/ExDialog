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
import com.tuuzed.androidx.dialog.internal.DialogButtonLayout
import com.tuuzed.androidx.dialog.internal.DialogTitle

inline fun ExDialog.Factory.showBasic(windowContext: Context, func: BasicController.() -> Unit) {
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
    private val dialogButtons: DialogButtonLayout
    private val dialogContentLayout: FrameLayout

    init {

        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.basic_dialog_layout, null, false)

        dialogTitle = view.findViewById(R.id.dialog_title)
        dialogButtons = view.findViewById(R.id.dialog_buttons)
        dialogContentLayout = view.findViewById(R.id.dialog_content_layout)

        attachView(view)
    }

    fun customView(@LayoutRes layoutId: Int) {
        LayoutInflater.from(dialog.windowContext).inflate(layoutId, dialogContentLayout, true)
    }

    fun customView(view: View) {
        dialogContentLayout.removeAllViews()
        dialogContentLayout.addView(view)
    }

    override fun icon(resId: Int?, icon: Drawable?) {
        dialogTitle.visibility = View.VISIBLE
        if (resId != null) {
            dialogTitle.setIcon(resId)
        } else {
            icon?.also { dialogTitle.setIcon(it) }
        }
    }


    override fun title(resId: Int?, text: CharSequence?, @ColorInt color: Int?) {
        dialogTitle.visibility = View.VISIBLE
        if (resId != null) {
            dialogTitle.setText(resId, color)
        } else {
            text?.also { dialogTitle.setText(it, color) }
        }

        dialogTitle.setText(text, color)
    }

    override fun positiveButton(
        text: CharSequence, @ColorInt color: Int?,
        icon: Drawable?,
        click: DialogButtonClick
    ) {
        dialogButtons.visibility = View.VISIBLE
        dialogButtons.dialogButton(
            DialogButtonLayout.BUTTON_NEUTRAL,
            text, color, icon,
            View.OnClickListener { click.invoke(dialog, ExDialog.BUTTON_POSITIVE) }
        )
    }

    override fun negativeButton(
        text: CharSequence, @ColorInt color: Int?,
        icon: Drawable?,
        click: DialogButtonClick
    ) {
        dialogButtons.visibility = View.VISIBLE
        dialogButtons.dialogButton(

            DialogButtonLayout.BUTTON_NEUTRAL,
            text, color, icon,
            View.OnClickListener { click.invoke(dialog, ExDialog.BUTTON_NEGATIVE) }
        )
    }

    override fun neutralButton(
        text: CharSequence, @ColorInt color: Int?,
        icon: Drawable?,
        click: DialogButtonClick
    ) {
        dialogButtons.visibility = View.VISIBLE
        dialogButtons.dialogButton(
            DialogButtonLayout.BUTTON_NEUTRAL,
            text, color, icon,
            View.OnClickListener { click.invoke(dialog, ExDialog.BUTTON_NEUTRAL) }
        )
    }

    override fun disablePositiveButton(disable: Boolean) {
        dialogButtons.disableDialogButton(DialogButtonLayout.BUTTON_POSITIVE, disable)
    }

    override fun disableNegativeButton(disable: Boolean) {
        dialogButtons.disableDialogButton(DialogButtonLayout.BUTTON_NEGATIVE, disable)
    }

    override fun disableNeutralButton(disable: Boolean) {
        dialogButtons.disableDialogButton(DialogButtonLayout.BUTTON_NEUTRAL, disable)

    }

}