package com.tuuzed.androidx.exdialog.input

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText

internal fun toggleSoftInput(editText: EditText, show: Boolean) {
    val context = editText.context
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    if (imm != null) {
        if (show) {
            imm.showSoftInput(editText, SHOW_IMPLICIT)
        } else {
            imm.hideSoftInputFromWindow(editText.windowToken, HIDE_NOT_ALWAYS)
        }
    }
}