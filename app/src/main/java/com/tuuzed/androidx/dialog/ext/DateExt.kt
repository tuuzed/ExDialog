@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import java.util.*

@SuppressLint("InflateParams")
fun ExDialog.date(func: DateConfigurator.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_date, null, false)
        val configurator = DateConfigurator(this@date, this, view)
        func(configurator)
        customView(view)
    }
}

class DateConfigurator(
    private val dialog: ExDialog,
    private val configurator: CustomViewConfigurator,
    private val view: View
) : DialogConfiguratorInterface by configurator {

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

    override fun positiveButton(text: CharSequence?, color: Int?, icon: Drawable?, click: ButtonClick) {
        configurator.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(datePicker.date)
            click(dialog, which)
        }
    }

}