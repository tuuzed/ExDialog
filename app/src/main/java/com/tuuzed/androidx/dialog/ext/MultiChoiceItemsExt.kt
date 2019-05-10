@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import com.tuuzed.androidx.dialog.ExDialog

fun ExDialog.multiChoiceItems(func: MultiChoiceItemsConfigurator.() -> Unit) {
    lists {
    }
}

class MultiChoiceItemsConfigurator(
    private val dialog: ExDialog,
    private val configurator: ListsConfigurator
) : DialogConfiguratorInterface by configurator