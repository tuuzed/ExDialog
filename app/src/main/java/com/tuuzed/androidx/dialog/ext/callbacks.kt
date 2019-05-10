package com.tuuzed.androidx.dialog.ext

import com.tuuzed.androidx.dialog.ExDialog
import java.util.*

typealias ButtonClick = (dialog: ExDialog, which: Int) -> Unit

typealias DateDialogCallback = (date: Date) -> Unit

typealias InputDialogCallback = (text: CharSequence) -> Unit

typealias DateRangeDialogCallback = (beginDate: Date, endDate: Date) -> Unit

typealias ItemsDialogCallback = (dialog: ExDialog, item: CharSequence, position: Int) -> Unit
