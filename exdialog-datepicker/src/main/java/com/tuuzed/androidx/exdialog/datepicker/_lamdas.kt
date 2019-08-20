package com.tuuzed.androidx.exdialog.datepicker

import com.tuuzed.androidx.exdialog.ExDialog
import java.util.*

typealias DatePickerCallback = ((dialog: ExDialog, date: Date) -> Unit)?
typealias DatePickerWatcher = ((dialog: ExDialog, date: Date) -> Unit)?

typealias DateRangePickerCallback = ((dialog: ExDialog, beginDate: Date, endDate: Date) -> Unit)?
typealias DateRangePickerWatcher = ((dialog: ExDialog, beginDate: Date, endDate: Date) -> Unit)?
