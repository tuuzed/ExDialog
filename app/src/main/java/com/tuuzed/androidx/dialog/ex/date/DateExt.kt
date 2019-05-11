@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ex.date

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ex.basic.CustomViewNamespace
import com.tuuzed.androidx.dialog.ex.basic.DialogButtonClick
import com.tuuzed.androidx.dialog.ex.basic.DialogNamespaceInterface
import com.tuuzed.androidx.dialog.ex.basic.customView
import java.util.*

@SuppressLint("InflateParams")
fun ExDialog.date(func: DateNamespace.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_date, null, false)
        val namespace = DateNamespace(this@date, this, view)
        func(namespace)
        customView(view)
    }
}

class DateNamespace(
    private val dialog: ExDialog,
    private val delegate: CustomViewNamespace,
    private val view: View
) : DialogNamespaceInterface by delegate {

    private val datePicker: DatePicker = view.findViewById(R.id.datePicker)

    private var callback: DateDialogCallback? = null

    fun maxYear(maxYear: Int) {
        datePicker.setMaxYear(maxYear)
        datePicker.type
    }

    fun minYear(minYear: Int) {
        datePicker.setMinYear(minYear)
    }

    fun type(@DatePickerType type: Int) {
        datePicker.type = type
    }

    fun date(date: Date) {
        datePicker.date = date
    }

    fun onDateChanged(callback: DateDialogCallback) {
        datePicker.setOnDateChangedListener { callback(it) }
    }

    fun callback(callback: DateDialogCallback) {
        this.callback = callback
    }

    override fun positiveButton(
        text: CharSequence,
        color: Int?,
        icon: Drawable?,
        click: DialogButtonClick?
    ) {
        delegate.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(datePicker.dateFormat.let { it.parse(it.format(datePicker.date)) })
            click?.invoke(dialog, which)
        }
    }

}