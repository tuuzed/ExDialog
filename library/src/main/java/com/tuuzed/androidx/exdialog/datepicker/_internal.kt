package com.tuuzed.androidx.exdialog.datepicker

import android.content.Context
import androidx.annotation.StringRes
import com.tuuzed.androidx.datepicker.DatePicker
import java.text.DateFormat

internal val DatePicker.dateFormat: DateFormat
    get() {
        return DatePicker.getDateFormat(this.datePickerType)
    }

internal fun Context.resString(@StringRes redId: Int, vararg formatArgs: Any) =
    this.getString(redId, *formatArgs)
