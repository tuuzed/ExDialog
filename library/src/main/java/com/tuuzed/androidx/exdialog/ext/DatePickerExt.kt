@file:JvmName("ExDialogWrapper")
@file:JvmMultifileClass

@file:Suppress("unused", "CanBeParameter", "InflateParams")

package com.tuuzed.androidx.exdialog.ext

import android.view.LayoutInflater
import android.view.View
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import java.util.*

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
    private var callback: DatePickerCallback? = null

    init {
        val inflater = LayoutInflater.from(dialog.windowContext)
        val view = inflater.inflate(R.layout.part_dialog_datepicker, null, false)
        datePicker = view.findViewById(R.id.datePicker)
        attachView(view)
    }

    fun yearRange(max: Int = -1, min: Int = -1) {
        if (max > 0) {
            datePicker.setMaxYear(max)
        }
        if (min > 0) {
            datePicker.setMinYear(min)
        }
    }

    fun datePickerType(@DatePickerType type: Int) {
        datePicker.datePickerType = type
    }

    fun date(date: Date) {
        datePicker.date = date
    }

    fun onDateChanged(callback: DatePickerCallback) {
        datePicker.setOnDateChangedListener { callback(dialog, it) }
    }

    fun callback(callback: DatePickerCallback) {
        this.callback = callback
    }

    override fun positiveButton(textRes: Int?, text: CharSequence?, click: DialogButtonClick?) {
        delegate.positiveButton(textRes, text) {
            callback?.invoke(dialog, datePicker.dateFormat.let { it.parse(it.format(datePicker.date)) })
            click?.invoke(dialog)
        }
    }
}