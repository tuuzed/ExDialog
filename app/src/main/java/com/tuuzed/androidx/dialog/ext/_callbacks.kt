package com.tuuzed.androidx.dialog.ext

import com.tuuzed.androidx.dialog.ExDialog
import java.util.*

typealias InputCallback = (text: CharSequence) -> Unit
typealias DialogButtonClick = (dialog: ExDialog, which: Int) -> Unit
typealias DateCallback = (date: Date) -> Unit
typealias DateRangeCallback = (beginDate: Date, endDate: Date) -> Unit
typealias ItemsCallback<T> = (dialog: ExDialog, selected: T, index: Int) -> Unit
typealias SingleChoiceItemsCallback<T> = (dialog: ExDialog, selected: T, index: Int) -> Unit
typealias MultiChoiceItemsCallback<T> = (dialog: ExDialog, selectedList: List<T>, indexList: List<Int>) -> Unit