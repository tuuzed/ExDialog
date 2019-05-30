package com.tuuzed.androidx.exdialog.input

import com.tuuzed.androidx.exdialog.ExDialog


typealias TextValidator = (text: CharSequence, errorText: Array<out CharSequence>) -> Boolean

typealias TextWatcher = (dialog: ExDialog, text: CharSequence) -> Unit

typealias InputCallback = (dialog: ExDialog, text: CharSequence) -> Unit

