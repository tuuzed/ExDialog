package com.tuuzed.androidx.exdialog.datepicker

import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

internal val DatePicker.dateFormat: DateFormat
    get() {
        return when (this.datePickerType) {
            DatePickerType.TYPE_Y -> SimpleDateFormat("yyyy", Locale.CHINA)
            DatePickerType.TYPE_YM -> SimpleDateFormat("yyyy-MM", Locale.CHINA)
            DatePickerType.TYPE_YMD -> SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            DatePickerType.TYPE_YMDH -> SimpleDateFormat("yyyy-MM-dd HH", Locale.CHINA)
            DatePickerType.TYPE_YMDHM -> SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
            else -> SimpleDateFormat("yyyy-MM-hh HH:mm", Locale.CHINA)
        }
    }