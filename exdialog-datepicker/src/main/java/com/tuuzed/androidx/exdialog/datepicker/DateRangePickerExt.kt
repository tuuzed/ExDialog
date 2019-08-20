package com.tuuzed.androidx.exdialog.datepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.TextView
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.exdialog.ExDialog
import java.util.*


@Suppress("LocalVariableName", "NAME_SHADOWING")
@SuppressLint("InflateParams", "SetTextI18n")
fun ExDialog.dateRangePicker(
    @DatePickerType datePickerType: Int = DatePickerType.TYPE_YMDHM,
    minYear: Int = -1,
    maxYear: Int = -1,
    beginDate: Date = Date(),
    endDate: Date = Date(),
    watcher: DateRangePickerWatcher = null,
    callback: DateRangePickerCallback = null
) {
    val type = "ExDialog#dateRangePicker".hashCode()
    customViewType = type

    var selectedBegin = true
    var beginDate = beginDate
    var endDate = endDate
    val customView = LayoutInflater.from(context).inflate(
        R.layout.exdialog_daterangepicker, null, false
    )
    val datePicker: DatePicker = customView.findViewById(R.id.date_picker)
    val beginText: TextView = customView.findViewById(R.id.begin_text)
    val endText: TextView = customView.findViewById(R.id.end_text)

    if (minYear != -1) datePicker.setMinYear(minYear)
    if (maxYear != -1) datePicker.setMinYear(maxYear)
    datePicker.datePickerType = datePickerType
    datePicker.setOnDateChangedListener { date ->
        if (selectedBegin) {
            beginDate = date
            beginText.text = context.resString(R.string.begin_date, formatDate(beginDate, datePickerType))
        } else {
            endDate = date
            endText.text = context.resString(R.string.end_date, formatDate(endDate, datePickerType))
        }
        setPositiveButtonEnable(beginDate.time <= endDate.time)
        watcher?.invoke(
            this,
            parseDate(formatDate(beginDate, datePickerType), datePickerType),
            parseDate(formatDate(endDate, datePickerType), datePickerType)
        )
    }
    beginText.text = context.resString(R.string.begin_date, formatDate(beginDate, datePickerType))
    endText.text = context.resString(R.string.end_date, formatDate(endDate, datePickerType))

    beginText.setOnClickListener {
        selectedBegin = true
        beginText.alpha = 1.0f
        endText.alpha = 0.2f
        datePicker.date = beginDate
    }
    endText.setOnClickListener {
        selectedBegin = false
        beginText.alpha = 0.2f
        endText.alpha = 1.0f
        datePicker.date = endDate
    }
    beginText.performClick()
    customView(view = customView)
    addEventWatcher { _, event ->
        if (event == ExDialog.ON_CLICK_POSITIVE_BUTTON && customViewType == type) {
            callback?.invoke(
                this,
                parseDate(formatDate(beginDate, datePickerType), datePickerType),
                parseDate(formatDate(endDate, datePickerType), datePickerType)
            )
        }
    }
}
