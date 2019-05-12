@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.DialogButtonLayout
import com.tuuzed.androidx.exdialog.internal.DialogLayout
import com.tuuzed.androidx.exdialog.internal.DialogTitle
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface

inline fun ExDialog.basic(func: BasicController.() -> Unit) {
    func(BasicController(this) { setContentView(it) })
}


class BasicController(
    private val dialog: ExDialog,
    attachView: (View) -> Unit
) : ExDialogInterface by dialog,
    BasicControllerInterface {

    private val dialogLayout: DialogLayout
    private val dialogTitle: DialogTitle
    private val dialogButtons: DialogButtonLayout
    private val dialogContent: FrameLayout

    init {

        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.basic_dialog_layout, null, false)

        dialogLayout = view.findViewById(R.id.dialog_layout)

        dialogTitle = dialogLayout.dialogTitle
        dialogButtons = dialogLayout.dialogButtons
        dialogContent = dialogLayout.dialogContent

        attachView(view)
    }

    fun customView(@LayoutRes layoutId: Int) {
        LayoutInflater.from(dialog.windowContext).inflate(layoutId, dialogContent, true)
    }

    fun customView(view: View) {
        dialogContent.removeAllViews()
        dialogContent.addView(view)
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
            DialogButtonLayout.BUTTON_POSITIVE,
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
            DialogButtonLayout.BUTTON_NEGATIVE,
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