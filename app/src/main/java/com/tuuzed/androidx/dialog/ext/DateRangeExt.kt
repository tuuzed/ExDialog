@file:Suppress("unused", "CanBeParameter")

package com.tuuzed.androidx.dialog.ext

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import java.util.*

@SuppressLint("InflateParams")
fun ExDialog.dateRange(func: DateRangeConfigurator.() -> Unit) {
    customView {
        val inflater = LayoutInflater.from(windowContext)
        val view = inflater.inflate(R.layout.part_dialog_daterange, null, false)
        val configurator = DateRangeConfigurator(this@dateRange, this, view)
        func(configurator)
        customView(view)
    }
}

class DateRangeConfigurator(
    private val dialog: ExDialog,
    private val configurator: CustomViewConfigurator,
    private val view: View
) : DialogConfiguratorInterface by configurator {

    private val beginText: TextView = view.findViewById(R.id.beginText)
    private val endText: TextView = view.findViewById(R.id.endText)
    private val datePicker: DatePicker = view.findViewById(R.id.datePicker)

    private var callback: DateRangeDialogCallback? = null
    private var dateChangedCallback: DateRangeDialogCallback? = null

    private var beginDate = Date()
    private var endDate = Date()

    init {

    }

    fun beginDate(date: Date) {
        beginDate = date
    }

    fun endDate(date: Date) {
        endDate = date
    }

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

    fun onDateChanged(callback: DateRangeDialogCallback) {
        this.dateChangedCallback = callback
    }

    fun callback(callback: DateRangeDialogCallback) {
        this.callback = callback
    }

    override fun positiveButton(text: CharSequence?, color: Int?, icon: Drawable?, click: ButtonClick) {
        configurator.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(beginDate, endDate)
            click(dialog, which)
        }
    }

}