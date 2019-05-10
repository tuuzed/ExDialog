package com.tuuzed.androidx.dialog.ext

import android.view.LayoutInflater
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

fun ExDialog.multiChoiceItems(func: MultiChoiceItemsOptions.() -> Unit) {
    basic {
        val itemsView = LayoutInflater.from(windowContext).inflate(R.layout.part_dialog_items, null, false)
        customView(itemsView)
        val options = MultiChoiceItemsOptions(this@multiChoiceItems, this)
        func(options)
    }
}

class MultiChoiceItemsOptions(
    private val dialog: ExDialog,
    private val basicOptions: BasicOptions
) : DialogOptionsInterface by basicOptions