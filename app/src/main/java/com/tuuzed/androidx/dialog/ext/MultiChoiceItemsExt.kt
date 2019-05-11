@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ListsControllerInterface

inline fun ExDialog.Factory.multiChoiceItems(windowContext: Context, func: MultiChoiceItemsController.() -> Unit) {
    ExDialog.show(windowContext) { multiChoiceItems(func) }
}


inline fun ExDialog.multiChoiceItems(func: MultiChoiceItemsController.() -> Unit) {
    lists {
        func(MultiChoiceItemsController(this@multiChoiceItems, this))
    }
}

class MultiChoiceItemsController(
    private val dialog: ExDialog,
    private val delegate: ListsController
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate,
    ListsControllerInterface by delegate