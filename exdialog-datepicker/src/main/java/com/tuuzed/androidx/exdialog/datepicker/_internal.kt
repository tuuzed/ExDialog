package com.tuuzed.androidx.exdialog.datepicker

import android.content.Context
import androidx.annotation.StringRes
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import java.text.DateFormat
import java.util.*

internal fun formatDate(date: Date, @DatePickerType datePickerType: Int): String =
    DatePicker.getDateFormat(datePickerType).format(date)

internal fun parseDate(source: String, @DatePickerType datePickerType: Int): Date =
    DatePicker.getDateFormat(datePickerType).parse(source)

internal fun Context.resString(@StringRes redId: Int, vararg formatArgs: Any) =
    this.getString(redId, *formatArgs)
