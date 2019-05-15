@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface


fun ExDialog.input(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    helperText: CharSequence? = null,
    hint: CharSequence? = null,
    maxLength: Int? = null,
    inputType: Int? = null,
    prefill: CharSequence? = null,
    callback: InputCallback? = null,
    onTextChanged: InputCallback? = null,
    func: (InputController.() -> Unit)? = null
) {
    customView(titleRes, title, iconRes, icon) {
        InputController(this@input, this) {
            customView(it)
        }.also {
            it.helperText(helperText)
            it.hint(hint)
            it.maxLength(maxLength)
            it.inputType(inputType)
            it.prefill(text = prefill)
            it.callback(callback)
            it.onTextChanged(onTextChanged)
            func?.invoke(it)
        }
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
        // 获取焦点，自动弹出软键盘
        textInputEditText.requestFocus()
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
        textInputEditText.setSelection(textInputEditText.text?.length ?: 0)
    }

    fun callback(callback: InputCallback?) {
        this.callback = callback
    }

    fun onTextChanged(callback: InputCallback?) {
        onTextChangedCallback = callback
    }

    override fun positiveButton(
        textRes: Int?, text: CharSequence?,
        iconRes: Int?, icon: Drawable?,
        colorRes: Int?, color: Int?,
        click: DialogButtonClick?
    ) {
        onTextChangedCallback?.invoke(dialog, textInputEditText.text ?: "")
        delegate.positiveButton(textRes, text, iconRes, icon, colorRes, color) {
            callback?.invoke(dialog, textInputEditText.text ?: "")
            click?.invoke(dialog)
        }
    }

}
