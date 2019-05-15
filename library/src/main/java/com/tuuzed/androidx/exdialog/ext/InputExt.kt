@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
    private var onTextChangedCallback: InputCallback? = null
    private val textInputLayout: TextInputLayout
    private val textInputEditText: TextInputEditText

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_input, null, false)
        textInputLayout = view.findViewById(R.id.textInputLayout)
        textInputEditText = view.findViewById(R.id.textInputEditText)
        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChangedCallback?.invoke(dialog, s ?: "")
            }
        })
        attachView(view)
    }


    fun error(text: CharSequence? = null) {
        textInputLayout.error = text
        textInputLayout.isErrorEnabled = text != null
    }

    fun helperText(text: CharSequence? = null) {
        textInputLayout.helperText = text
        textInputLayout.isHelperTextEnabled = text != null
    }

    fun hint(text: CharSequence? = null, enableAnimation: Boolean = true) {
        textInputLayout.hint = text
        textInputLayout.isHintEnabled = text != null
        textInputLayout.isHintAnimationEnabled = enableAnimation
    }

    fun maxLength(maxLength: Int? = null) {
        textInputLayout.counterMaxLength = maxLength ?: -1
        textInputLayout.isCounterEnabled = maxLength != null
    }

    fun inputType(inputType: Int? = null) {
        inputType?.let { textInputEditText.inputType = it }
    }

    fun prefill(@StringRes textRes: Int? = null, text: CharSequence? = null) {
        textRes?.let { textInputEditText.setText(textRes) }
        textInputEditText.setText(text)
    }

    fun callback(callback: InputCallback) {
        this.callback = callback
    }

    fun onTextChanged(callback: InputCallback) {
        onTextChangedCallback = callback
        callback(dialog, textInputEditText.text ?: "")
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
            callback?.invoke(dialog, textInputEditText.text ?: "")
            click(dialog)
        }
    }
}
