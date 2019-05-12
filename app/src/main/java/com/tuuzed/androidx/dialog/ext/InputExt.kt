@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorInt
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface

inline fun ExDialog.Factory.showInput(windowContext: Context, func: InputController.() -> Unit) {
    ExDialog.show(windowContext) { input(func) }
}

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

    fun prefillText(text: CharSequence) {
        editText.setText(text)
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
