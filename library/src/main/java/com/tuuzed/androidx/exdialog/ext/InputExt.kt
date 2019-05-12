@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorInt
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

    @JvmOverloads
    fun hint(@StringRes resId: Int = View.NO_ID, text: CharSequence? = null) {
        if (resId != View.NO_ID) {
            editText.setHint(resId)
        } else {
            text?.also { editText.hint = text }
        }
    }

    @JvmOverloads
    fun prefillText(@StringRes resId: Int = View.NO_ID, text: CharSequence? = null) {
        if (resId != View.NO_ID) {
            editText.setText(resId)
        } else {
            text?.also { editText.setText(text) }
        }
        editText.setSelection(editText.text?.length ?: 0)
    }

    fun callback(callback: InputCallback) {
        this.callback = callback
    }

    override fun positiveButton(text: CharSequence, @ColorInt color: Int?, icon: Drawable?, click: DialogButtonClick) {
        delegate.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(editText.text)
            click.invoke(dialog, which)
        }
    }
}
