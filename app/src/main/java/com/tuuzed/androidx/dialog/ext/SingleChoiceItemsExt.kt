@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import com.tuuzed.androidx.dialog.ExDialog

fun ExDialog.singleChoiceItems(func: SingleChoiceItemsConfigurator.() -> Unit) {
    lists {
    }
}

class SingleChoiceItemsConfigurator(
    private val dialog: ExDialog,
    private val configurator: ListsConfigurator
) : DialogConfiguratorInterface by configurator