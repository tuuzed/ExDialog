@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ex.lists

import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.ex.basic.DialogNamespaceInterface

fun ExDialog.singleChoiceItems(func: SingleChoiceItemsNamespace.() -> Unit) {
    lists {
        val namespace = SingleChoiceItemsNamespace(this@singleChoiceItems, this)
        func(namespace)
    }
}

class SingleChoiceItemsNamespace(
    private val dialog: ExDialog,
    private val delegate: ListsNamespace
) : DialogNamespaceInterface by delegate