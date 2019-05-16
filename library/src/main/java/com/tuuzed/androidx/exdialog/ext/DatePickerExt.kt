package com.tuuzed.androidx.exdialog.ext

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tuuzed.androidx.datepicker.DatePicker
import com.tuuzed.androidx.datepicker.DatePickerType
import com.tuuzed.androidx.exdialog.ExDialog
import com.tuuzed.androidx.exdialog.R
import com.tuuzed.androidx.exdialog.internal.interfaces.BasicControllerInterface
import com.tuuzed.androidx.exdialog.internal.interfaces.ExDialogInterface
import java.util.*

fun ExDialog.datePicker(
    @StringRes titleRes: Int? = null, title: CharSequence? = null,
    @DrawableRes iconRes: Int? = null, icon: Drawable? = null,
    //
    @DatePickerType datePickerType: Int? = null,
    minYear: Int? = null,
    maxYear: Int? = null,
    date: Date? = null,
    onDateChanged: DatePickerCallback? = null,
    callback: DatePickerCallback? = null,
    //
    func: (DateController.() -> Unit)? = null
) {
    customView(titleRes, title, iconRes, icon) {
        DateController(this@datePicker, this) {
            customView(it)
        }.apply {
            datePickerType?.let { datePickerType(it) }
            minYear?.let { minYear(it) }
            maxYear?.let { maxYear(it) }
            date?.let { date(it) }
            onDateChanged?.let { onDateChanged(it) }
            callback?.let { callback(it) }
            func?.invoke(this)
        }

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

    fun minYear(minYear: Int? = null) {
        minYear?.let { datePicker.setMinYear(it) }
    }

    fun maxYear(maxYear: Int? = null) {
        maxYear?.let { datePicker.setMaxYear(it) }
    }

    fun datePickerType(@DatePickerType type: Int) {
        datePicker.datePickerType = type
    }

    fun date(date: Date?) {
        datePicker.date = date ?: Date()
    }

    fun onDateChanged(callback: DatePickerCallback?) {
        datePicker.setOnDateChangedListener { callback?.invoke(dialog, it) }
    }

    fun callback(callback: DatePickerCallback?) {
        this.callback = callback
    }

    override fun positiveButton(
        textRes: Int?, text: CharSequence?,
        iconRes: Int?, icon: Drawable?,
        colorRes: Int?, color: Int?,
        click: DialogButtonClick?
    ) {
        delegate.positiveButton(textRes, text, iconRes, icon, colorRes, color) {
            callback?.invoke(dialog, datePicker.dateFormat.let { it.parse(it.format(datePicker.date)) })
            click?.invoke(dialog)
        }
    }
}