package com.tuuzed.androidx.exdialog

typealias ExDialogCallback = ((dialog: ExDialog) -> Unit)?

typealias ExDialogEventWatcher = (dialog: ExDialog, event: Int) -> Unit

typealias ItemsCallback =
        ((dialog: ExDialog, index: Int) -> Unit)?

typealias SingleChoiceItemsWatcher =
        ((dialog: ExDialog, index: Int) -> Unit)?

typealias SingleChoiceItemsCallback =
        ((dialog: ExDialog, index: Int) -> Unit)?

typealias MultiChoiceItemsWatcher =
        ((dialog: ExDialog, index: Int, isChecked: Boolean) -> Unit)?

typealias MultiChoiceItemsCallback =
        ((dialog: ExDialog, indices: IntArray) -> Unit)?