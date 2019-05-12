package com.tuuzed.androidx.exdialog.ext

import com.tuuzed.androidx.exdialog.ExDialog
import java.util.*

// Dialog

typealias DialogButtonClick = (dialog: ExDialog, which: Int) -> Unit

// Input

typealias InputCallback = (text: CharSequence) -> Unit

// DatePicker

typealias DatePickerCallback = (date: Date) -> Unit
typealias DateRangePickerCallback = (beginDate: Date, endDate: Date) -> Unit

// Lists

typealias ItemsCallback<T> = (dialog: ExDialog, index: Int, item: T, checked: Boolean) -> Unit
typealias SingleChoiceItemsCallback<T> = (dialog: ExDialog, index: Int, selectedItem: T?) -> Unit
typealias MultiChoiceItemsCallback<T> = (dialog: ExDialog, indices: List<Int>, selectedItems: List<T>) -> Unit