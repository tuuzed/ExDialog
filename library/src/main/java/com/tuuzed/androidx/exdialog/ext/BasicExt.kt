package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.DialogButtonLayout
import com.tuuzed.androidx.exdialog.internal.DialogLayout
import com.tuuzed.androidx.exdialog.internal.DialogTitle
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface

fun ExDialog.basic(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    func: (BasicController.() -> Unit)? = null
) {
    BasicController(this) {
        setContentView(it)
    }.apply {
        if (titleRes != null || title != null) {
            title(titleRes, title)
        }
        if (iconRes != null || icon != null) {
            icon(iconRes, icon)
        }
        func?.invoke(this)
    }
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

    override fun iconVisible(visible: Boolean) {
        dialogTitle.titleIcon.visibility = if (visible) View.VISIBLE else View.GONE
        dialogTitle.requestUpdateLayout()
    }

    override fun icon(iconRes: Int?, icon: Drawable?) {
        dialogTitle.titleIcon.also { titleIcon ->
            iconRes?.let { titleIcon.setImageResource(it) }
            icon?.let { titleIcon.setImageDrawable(it) }
            titleIcon.visibility = View.VISIBLE
        }
        dialogTitle.requestUpdateLayout()
    }

    override fun titleVisible(visible: Boolean) {
        dialogTitle.titleText.visibility = if (visible) View.VISIBLE else View.GONE
        dialogTitle.requestUpdateLayout()
    }

    override fun title(textRes: Int?, text: CharSequence?) {
        dialogTitle.titleText.also { titleText ->
            textRes?.let { titleText.setText(it) }
            text?.let { titleText.text = it }
            titleText.visibility = View.VISIBLE
        }
        dialogTitle.requestUpdateLayout()
    }

    // ===
    override fun positiveButtonEnable(enable: Boolean) {
        val button = dialogButtonLayout.positiveButton
        button.isEnabled = enable
    }

    override fun positiveButtonVisible(visible: Boolean) {
        val button = dialogButtonLayout.positiveButton
        button.visibility = if (visible) View.VISIBLE else View.GONE
        dialogButtonLayout.requestUpdateLayout()
    }


    override fun positiveButton(
        textRes: Int?, text: CharSequence?,
        iconRes: Int?, icon: Drawable?,
        colorRes: Int?, color: Int?,
        click: DialogButtonClick?
    ) {
        val button = dialogButtonLayout.positiveButton
        textRes?.let { button.setText(it) }
        text?.let { button.text = it }

        iconRes?.let { button.setIconResource(resColor(it)) }
        icon?.let { button.icon = it }

        colorRes?.let { button.setButtonColor(resColor(it)) }
        color?.let { button.setButtonColor(it) }

        button.visibility = View.VISIBLE
        button.setOnClickListener { click?.invoke(dialog) }
        dialogButtonLayout.requestUpdateLayout()
    }

    // ===
    override fun negativeButtonEnable(enable: Boolean) {
        val button = dialogButtonLayout.negativeButton
        button.isEnabled = enable
    }

    override fun negativeButtonVisible(visible: Boolean) {
        val button = dialogButtonLayout.negativeButton
        button.visibility = if (visible) View.VISIBLE else View.GONE
        dialogButtonLayout.requestUpdateLayout()
    }

    override fun negativeButton(
        textRes: Int?, text: CharSequence?,
        iconRes: Int?, icon: Drawable?,
        colorRes: Int?, color: Int?,
        click: DialogButtonClick?
    ) {
        val button = dialogButtonLayout.negativeButton
        textRes?.let { button.setText(it) }
        text?.let { button.text = it }

        iconRes?.let { button.setIconResource(resColor(it)) }
        icon?.let { button.icon = it }

        colorRes?.let { button.setButtonColor(resColor(it)) }
        color?.let { button.setButtonColor(it) }

        button.visibility = View.VISIBLE
        button.setOnClickListener { click?.invoke(dialog) }
        dialogButtonLayout.requestUpdateLayout()
    }


    // ===

    override fun neutralButtonEnable(enable: Boolean) {
        val button = dialogButtonLayout.neutralButton
        button.isEnabled = enable
    }

    override fun neutralButtonVisible(visible: Boolean) {
        val button = dialogButtonLayout.neutralButton
        button.visibility = if (visible) View.VISIBLE else View.GONE
        dialogButtonLayout.requestUpdateLayout()
    }

    override fun neutralButton(
        textRes: Int?, text: CharSequence?,
        iconRes: Int?, icon: Drawable?,
        colorRes: Int?, color: Int?,
        click: DialogButtonClick?
    ) {
        val button = dialogButtonLayout.neutralButton
        textRes?.let { button.setText(it) }
        text?.let { button.text = it }

        iconRes?.let { button.setIconResource(resColor(it)) }
        icon?.let { button.icon = it }

        colorRes?.let { button.setButtonColor(resColor(it)) }
        color?.let { button.setButtonColor(it) }

        button.visibility = View.VISIBLE
        button.setOnClickListener { click?.invoke(dialog) }
        dialogButtonLayout.requestUpdateLayout()
    }


    private fun resColor(@ColorRes colorRes: Int): Int =
        ResourcesCompat.getColor(dialog.windowContext.resources, colorRes, dialog.windowContext.theme)
}