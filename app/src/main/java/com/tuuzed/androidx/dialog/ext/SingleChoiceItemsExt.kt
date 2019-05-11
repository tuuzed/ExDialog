@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ListsControllerInterface

inline fun ExDialog.Factory.singleChoiceItems(windowContext: Context, func: SingleChoiceItemsController.() -> Unit) {
    ExDialog.show(windowContext) { singleChoiceItems(func) }
}

inline fun ExDialog.singleChoiceItems(func: SingleChoiceItemsController.() -> Unit) {
    lists {
        func(SingleChoiceItemsController(this@singleChoiceItems, this))
    }
}

class SingleChoiceItemsController(
    private val dialog: ExDialog,
    private val delegate: ListsController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate
