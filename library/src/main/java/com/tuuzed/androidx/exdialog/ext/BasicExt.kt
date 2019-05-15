@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.DialogButton
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
    private val dialogButtonLayout: DialogButtonLayout
    private val dialogContentLayout: FrameLayout

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.basic_dialog_layout, null, false)

        dialogLayout = view.findViewById(R.id.dialog_layout)

        dialogTitle = dialogLayout.dialogTitle.apply {
            this.visibility = View.GONE
            titleIcon.visibility = View.GONE
            titleText.visibility = View.GONE
        }
        dialogButtonLayout = dialogLayout.dialogButtonLayout.apply {
            this.visibility = View.GONE
            positiveButton.visibility = View.GONE
            negativeButton.visibility = View.GONE
            neutralButton.visibility = View.GONE
        }
        dialogContentLayout = dialogLayout.dialogContentLayout

        attachView(view)
    }

    fun customView(@LayoutRes layoutId: Int) {
        LayoutInflater.from(dialog.windowContext).inflate(layoutId, dialogContentLayout, true)
    }

    fun customView(view: View) {
        dialogContentLayout.also {
            it.removeAllViews()
            it.addView(view)
        }
    }

    override fun icon(iconRes: Int?, icon: Drawable?, visible: Boolean?) {
        dialogTitle.titleIcon.also { titleIcon ->
            iconRes?.let { titleIcon.setImageResource(it) }
            icon?.let { titleIcon.setImageDrawable(it) }

            visible?.let { titleIcon.visibility = if (it) View.VISIBLE else View.GONE }
        }
        dialogTitle.requestUpdateLayout()
    }

    override fun title(textRes: Int?, text: CharSequence?, colorRes: Int?, color: Int?, visible: Boolean?) {
        dialogTitle.titleText.also { titleText ->
            textRes?.let { titleText.setText(it) }
            text?.let { titleText.text = it }

            colorRes?.let { titleText.setTextColor(resColor(it)) }
            color?.let { titleText.setTextColor(it) }

            visible?.let { titleText.visibility = if (it) View.VISIBLE else View.GONE }

        }
        dialogTitle.requestUpdateLayout()
    }

    override fun positiveButton(
        textRes: Int?,
        text: CharSequence?,
        colorRes: Int?,
        color: Int?,
        iconRes: Int?,
        icon: Drawable?,
        enable: Boolean?,
        visible: Boolean?,
        click: DialogButtonClick?
    ) {
        setUpButton(
            dialogButtonLayout.positiveButton,
            textRes,
            text,
            colorRes,
            color,
            iconRes,
            icon,
            enable,
            visible,
            click
        )
    }

    override fun negativeButton(
        textRes: Int?,
        text: CharSequence?,
        colorRes: Int?,
        color: Int?,
        iconRes: Int?,
        icon: Drawable?,
        enable: Boolean?,
        visible: Boolean?,
        click: DialogButtonClick?
    ) {
        setUpButton(
            dialogButtonLayout.negativeButton,
            textRes,
            text,
            colorRes,
            color,
            iconRes,
            icon,
            enable,
            visible,
            click
        )
    }

    override fun neutralButton(
        textRes: Int?,
        text: CharSequence?,
        colorRes: Int?,
        color: Int?,
        iconRes: Int?,
        icon: Drawable?,
        enable: Boolean?,
        visible: Boolean?,
        click: DialogButtonClick?
    ) {
        setUpButton(
            dialogButtonLayout.neutralButton,
            textRes,
            text,
            colorRes,
            color,
            iconRes,
            icon,
            enable,
            visible,
            click
        )

    }


    private fun resColor(@ColorRes colorRes: Int): Int =
        ResourcesCompat.getColor(dialog.windowContext.resources, colorRes, dialog.windowContext.theme)

    private fun setUpButton(
        button: DialogButton,
        textRes: Int?,
        text: CharSequence?,
        colorRes: Int?,
        color: Int?,
        iconRes: Int?,
        icon: Drawable?,
        enable: Boolean?,
        visible: Boolean?,
        click: DialogButtonClick?
    ) {
        textRes?.let { button.setText(it) }
        text?.let { button.text = it }

        colorRes?.let { button.setButtonColor(resColor(it)) }
        color?.let { button.setButtonColor(it) }

        iconRes?.let { button.setIconResource(iconRes) }
        icon?.let { button.icon = icon }

        enable?.let { button.isEnabled = it }

        visible?.let { button.visibility = if (it) View.VISIBLE else View.GONE }

        button.setOnClickListener { click?.invoke(dialog) }

        dialogButtonLayout.requestUpdateLayout()
    }
}