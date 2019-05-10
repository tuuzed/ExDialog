package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

@SuppressLint("InflateParams")
fun ExDialog.dateChooser(func: DateChooserOptions.() -> Unit) {
    basic {
        val itemsView = LayoutInflater.from(windowContext).inflate(R.layout.part_dialog_items, null, false)
        customView(itemsView)
        val options = DateChooserOptions(this@dateChooser, this)
        func(options)
    }
}

class DateChooserOptions(
    private val dialog: ExDialog,
    private val basicOptions: BasicOptions
) : DialogOptionsInterface by basicOptions