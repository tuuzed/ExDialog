@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.dialog.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.dialog.ExDialog
import com.tuuzed.androidx.dialog.R
import com.tuuzed.androidx.dialog.ext.interfaces.BasicControllerInterface
import com.tuuzed.androidx.dialog.ext.interfaces.ExDialogInterface
import java.util.*

inline fun ExDialog.Factory.datePicker(windowContext: Context, func: DateController.() -> Unit) {
    ExDialog.show(windowContext) { datePicker(func) }
}

inline fun ExDialog.datePicker(func: DateController.() -> Unit) {
    customView {
        func(DateController(this@datePicker, this) { customView(it) })
    }
}

class DateController(
    private val dialog: ExDialog,
    private val delegate: CustomViewController,
    attachView: (View) -> Unit
) : ExDialogInterface by dialog,
    BasicControllerInterface by delegate {

    private val datePicker: DatePicker
    private var callback: DateCallback? = null

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_datepicker, null, false)
        datePicker = view.findViewById(R.id.datePicker)
        attachView(view)
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

    fun date(date: Date) {
        datePicker.date = date
    }

    fun onDateChanged(callback: DateCallback) {
        datePicker.setOnDateChangedListener { callback(it) }
    }

    fun callback(callback: DateCallback) {
        this.callback = callback
    }

    override fun positiveButton(text: CharSequence, color: Int?, icon: Drawable?, click: DialogButtonClick?) {
        delegate.positiveButton(text, color, icon) { dialog, which ->
            callback?.invoke(datePicker.dateFormat.let { it.parse(it.format(datePicker.date)) })
            click?.invoke(dialog, which)
        }
    }

}