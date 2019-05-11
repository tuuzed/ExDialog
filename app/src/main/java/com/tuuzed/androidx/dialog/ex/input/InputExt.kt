@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ex.input

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ex.basic.CustomViewNamespace
import com.tuuzed.androidx.dialog.ex.basic.DialogButtonClick
import com.tuuzed.androidx.dialog.ex.basic.DialogNamespaceInterface
import com.tuuzed.androidx.dialog.ex.basic.customView

@SuppressLint("InflateParams")
fun ExDialog.input(func: InputNamespace.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_input, null, false)
        val namespace = InputNamespace(this@input, this, view)
        func(namespace)
        customView(view)
    }
}


class InputNamespace(
    private val dialog: ExDialog,
    private val delegate: CustomViewNamespace,
    private val view: View
) : DialogNamespaceInterface by delegate {
    private val editText: EditText = view.findViewById(R.id.editText)

    private var callback: InputDialogCallback? = null

    fun inputType(inputType: Int) {
        editText.inputType = inputType
    }

    fun prefillText(text: CharSequence) {
        editText.setText(text)
    }

    fun callback(callback: InputDialogCallback) {
        this.callback = callback
    }

    override fun positiveButton(
        text: CharSequence,
        color: Int?,
        icon: Drawable?,
        click: DialogButtonClick?
    ) {
        delegate.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(editText.text)
            click?.invoke(dialog, which)
        }
    }
}
