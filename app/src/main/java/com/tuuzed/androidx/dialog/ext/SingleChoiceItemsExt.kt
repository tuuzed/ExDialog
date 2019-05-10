package com.tuuzed.androidx.dialog.ext

import android.view.LayoutInflater
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

fun ExDialog.singleChoiceItems(func: SingleChoiceItemsOptions.() -> Unit) {
    basic {
        val itemsView = LayoutInflater.from(windowContext).inflate(R.layout.part_dialog_items, null, false)
        customView(itemsView)
        val options = SingleChoiceItemsOptions(this@singleChoiceItems, this)
        func(options)
    }
}

class SingleChoiceItemsOptions(
    private val dialog: ExDialog,
    private val basicOptions: BasicOptions
) : DialogOptionsInterface by basicOptions