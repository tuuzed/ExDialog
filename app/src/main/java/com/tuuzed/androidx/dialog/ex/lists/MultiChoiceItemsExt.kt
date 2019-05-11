@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ex.lists

import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.ex.basic.DialogNamespaceInterface

fun ExDialog.multiChoiceItems(func: MultiChoiceItemsNamespace.() -> Unit) {
    lists {
    }
}

class MultiChoiceItemsNamespace(
    private val dialog: ExDialog,
    private val delegate: ListsNamespace
) : DialogNamespaceInterface by delegate