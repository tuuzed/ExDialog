@file:Suppress("unused")

package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R

@SuppressLint("InflateParams")
fun ExDialog.input(func: InputOptions.() -> Unit) {
    basic {
        val inputView = LayoutInflater.from(windowContext).inflate(R.layout.part_dialog_input, null, false)
        customView(inputView)
        val options = InputOptions(this@input, this)
        func(options)
    }
}


class InputOptions(
    private val dialog: ExDialog,
    private val basicOptions: BasicOptions
) : DialogOptionsInterface by basicOptions
