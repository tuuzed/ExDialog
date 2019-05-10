@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.view.LayoutInflater
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

fun ExDialog.items(func: ItemsOptions.() -> Unit) {
    basic {
        val itemsView = LayoutInflater.from(windowContext).inflate(R.layout.part_dialog_items, null, false)
        customView(itemsView)
        val options = ItemsOptions(this@items, this)
        func(options)
    }
}


class ItemsOptions(
    private val dialog: ExDialog,
    private val basicOptions: BasicOptions
) : DialogOptionsInterface by basicOptions





