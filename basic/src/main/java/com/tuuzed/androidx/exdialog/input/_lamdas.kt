package com.tuuzed.androidx.exdialog.input

import com.tuuzed.androidx.exdialog.ExDialog


typealias TextValidator = (dialog: ExDialog, text: CharSequence, errorText: Array<in CharSequence>) -> Boolean

typealias TextWatcher = (dialog: ExDialog, text: CharSequence) -> Unit

typealias InputCallback = (dialog: ExDialog, text: CharSequence) -> Unit

