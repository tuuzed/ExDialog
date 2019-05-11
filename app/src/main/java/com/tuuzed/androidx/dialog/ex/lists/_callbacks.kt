package com.tuuzed.androidx.dialog.ex.lists

import com.tuuzed.androidx.dialog.ExDialog

typealias ItemsDialogCallback<T> = (dialog: ExDialog, selected: T, index: Int) -> Unit

typealias SingleChoiceItemsDialogCallback<T> = (dialog: ExDialog, selected: T, index: Int) -> Unit

typealias MultiChoiceItemsDialogCallback<T> = (dialog: ExDialog, selectedList: List<T>, indexList: List<Int>) -> Unit
