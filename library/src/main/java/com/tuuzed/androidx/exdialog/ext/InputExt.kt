@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.StringRes
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface


inline fun ExDialog.input(func: InputController.() -> Unit) {
    customView {
        func(InputController(this@input, this) { customView(it) })
    }
}


class InputController(
    private val dialog: ExDialog,
    private val delegate: CustomViewController,
    attachView: (View) -> Unit
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate {

    private var callback: InputCallback? = null
    private val editText: EditText

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_input, null, false)
        editText = view.findViewById(R.id.editText)
        attachView(view)
    }

    fun inputType(inputType: Int) {
        editText.inputType = inputType
    }

    fun hint(@StringRes textRes: Int? = null, text: CharSequence? = null) {
        textRes?.also { editText.setHint(textRes) }
        text?.also { editText.hint = text }
    }

    fun prefillText(@StringRes textRes: Int? = null, text: CharSequence? = null) {
        textRes?.also { editText.setText(textRes) }
        text?.also { editText.setText(text) }
        editText.setSelection(editText.text?.length ?: 0)
    }

    fun callback(callback: InputCallback) {
        this.callback = callback
    }

    override fun positiveButton(
        textRes: Int?,
        text: CharSequence?,
        colorRes: Int?,
        color: Int?,
        iconRes: Int?,
        icon: Drawable?,
        enable: Boolean,
        visible: Boolean,
        click: DialogButtonClick
    ) {
        delegate.positiveButton(textRes, text, colorRes, color, iconRes, icon, enable, visible) {
            callback?.invoke(dialog, editText.text)
            click(dialog)
        }
    }
}
