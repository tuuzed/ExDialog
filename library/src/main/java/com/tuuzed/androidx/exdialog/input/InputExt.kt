package com.tuuzed.androidx.exdialog.input

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.ExDialogEvent
import com.tuuzed.androidx.exdialog.R

@SuppressLint("InflateParams")
fun ExDialog.input(
    prefill: CharSequence? = null,
    hint: CharSequence? = null,
    inputType: Int? = null,
    helperText: CharSequence? = null,
    maxLength: Int = -1,
    allowEmpty: Boolean = true,
    validator: TextValidator? = null,
    watcher: TextWatcher? = null,
    callback: InputCallback? = null
) {
    val flag = "ExDialog#input".hashCode()
    customViewFlag = flag

    val customView = LayoutInflater.from(context).inflate(
        R.layout.exdialog_edittext, null, false
    )
    val textInputLayout: TextInputLayout = customView.findViewById(R.id.textInputLayout)
    val textInputEditText: TextInputEditText = customView.findViewById(R.id.textInputEditText)
    val errorText = arrayOf("")
    textInputEditText.apply {
        setText(prefill)
        setSelection(prefill?.length ?: 0)
        setHint(hint)
        inputType?.also { setInputType(it) }
        setPositiveButtonEnable(!allowEmpty && text.isNullOrEmpty())
    }
    textInputLayout.apply {
        helperText?.also {
            isHelperTextEnabled = true
            setHelperText(helperText)
        }
        if (maxLength != -1) {
            isCounterEnabled = true
            counterMaxLength = maxLength
        }
    }
    textInputEditText.addTextChangedListener { text ->
        // 长度验证
        setPositiveButtonEnable(maxLength != -1 && text?.length ?: 0 > maxLength)
        // 空验证
        setPositiveButtonEnable(!allowEmpty && text.isNullOrEmpty())
        // 自定义验证器验证
        if (validator?.invoke(text ?: "", errorText) == true) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = errorText[0]
            setPositiveButtonEnable(false)
        } else {
            helperText?.also {
                textInputLayout.isHelperTextEnabled = true
                textInputLayout.helperText = helperText
            }
            setPositiveButtonEnable(true)
        }
        watcher?.invoke(this, text ?: "")
    }
    customView(view = customView)
    addEventWatcher { _, event ->
        when (event) {
            ExDialogEvent.ON_SHOW -> {
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                textInputEditText.requestFocus()
                textInputEditText.post {
                    toggleSoftInput(textInputEditText, true)
                }
            }
            ExDialogEvent.ON_DISMISS -> {
                toggleSoftInput(textInputEditText, false)
            }
            ExDialogEvent.ON_CLICK_POSITIVE_BUTTON -> {
                if (customViewFlag == flag) {
                    callback?.invoke(this, textInputEditText.text ?: "")
                }
            }
        }
    }
}