package com.tuuzed.androidx.exdialog.datepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.exdialog.ExDialog
import java.util.*


@SuppressLint("InflateParams")
fun ExDialog.datePicker(
    @DatePickerType datePickerType: Int = DatePickerType.TYPE_YMDHM,
    minYear: Int = -1,
    maxYear: Int = -1,
    date: Date = Date(),
    watcher: DatePickerWatcher = null,
    callback: DatePickerCallback = null
) {
    val type = "ExDialog#datePicker".hashCode()
    customViewType = type

    val customView = LayoutInflater.from(context).inflate(
        R.layout.exdialog_datepicker, null, false
    )
    val datePicker: DatePicker = customView.findViewById(R.id.date_picker)
    if (minYear != -1) datePicker.setMinYear(minYear)
    if (maxYear != -1) datePicker.setMinYear(maxYear)
    datePicker.datePickerType = datePickerType
    datePicker.date = date
    datePicker.setOnDateChangedListener { watcher?.invoke(this, it) }
    customView(view = customView)
    addEventWatcher { _, event ->
        if (event == ExDialog.ON_CLICK_POSITIVE_BUTTON && customViewType == type) {
            callback?.invoke(
                this,
                datePicker.dateFormat.let { it.parse(it.format(datePicker.date)) }
            )
        }
    }
}