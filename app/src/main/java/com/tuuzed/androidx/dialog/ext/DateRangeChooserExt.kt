package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

@SuppressLint("InflateParams")
fun ExDialog.dateRangeChooser(func: DateRangeChooserOptions.() -> Unit) {
    basic {
        val itemsView = LayoutInflater.from(windowContext).inflate(R.layout.part_dialog_daterange, null, false)
        customView(itemsView)
        val options = DateRangeChooserOptions(this@dateRangeChooser, this)
        func(options)
    }
}

class DateRangeChooserOptions(
    private val dialog: ExDialog,
    private val basicOptions: BasicOptions
) : DialogOptionsInterface by basicOptions